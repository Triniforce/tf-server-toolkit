/*
 * Copyright(C) Triniforce
 * All Rights Reserved.
 *
 */
package com.triniforce.server.plugins.kernel.ext.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.mail.Session;

import com.triniforce.db.test.BasicServerRunningTestCase;
import com.triniforce.server.srvapi.IThrdWatcherRegistrator;
import com.triniforce.utils.Api;
import com.triniforce.utils.ApiStack;
import com.triniforce.utils.TFUtils;

/*


test.properties must have the following settings:

Mailer.host=smtp.yandex.ru
Mailer.port=465
Mailer.user=triniforce@yandex.ru
Mailer.password=
Mailer.useTLS=1
Mailer.sender=triniforce@yandex.ru
Mailer.from=triniforce@yandex.ru
Mailer.to=maxim.ge@gmail.com
 
 */

public class MailerTest2 extends BasicServerRunningTestCase {
    public class MailerSettings implements IMailerSettings {

        private String m_smtpHost;
        private int m_smtpPort;
        private String m_smtpUser;
        private String m_smtpPassword;
        private boolean m_useTLS = false;
        private String m_sender;

        public MailerSettings() {
            m_smtpHost = getMustHaveTestProperty("Mailer.host");
            m_smtpPort = Integer
                    .parseInt(getMustHaveTestProperty("Mailer.port"));
            m_smtpUser = getMustHaveTestProperty("Mailer.user");
            m_smtpPassword = getMustHaveTestProperty("Mailer.password");
            m_useTLS = TFUtils.equals(1,
                    getMustHaveTestProperty("Mailer.useTLS"));
            m_sender = getMustHaveTestProperty("Mailer.sender");
        }

        public void loadSettings() {
        }

        public String getSmtpHost() {
            return m_smtpHost;
        }

        public int getSmtpPort() {
            return m_smtpPort;
        }

        public String getSmtpUser() {
            return m_smtpUser;
        }

        public String getSmtpPassword() {
            return m_smtpPassword;
        }

        public boolean useTLS() {
            return m_useTLS;
        }

		public String getDefaultSender() {
			return m_sender;
		}

		@Override
		public Session createSmtpSession() {
			// TODO Auto-generated method stub
			return null;
		}

    }
    
    boolean registered;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        IThrdWatcherRegistrator twr = ApiStack
                .getInterface(IThrdWatcherRegistrator.class);
        twr.registerThread(Thread.currentThread(), null);
        registered = true;
    }
    
    @Override
    protected void tearDown() throws Exception {
        if(registered){
            IThrdWatcherRegistrator twr = ApiStack
                .getInterface(IThrdWatcherRegistrator.class);
            twr.unregisterThread(Thread.currentThread());
            registered = false;
        }
        super.tearDown();
    }

    @Override
    public void test() throws Exception {

        String from = getMustHaveTestProperty("Mailer.from");
        String to = getMustHaveTestProperty("Mailer.to");

        Mailer mailer = new Mailer();
        Api api = new Api();

        // from rambler by SSLq
        api.setIntfImplementor(IMailerSettings.class, new MailerSettings());
        ApiStack.pushApi(api);
        try {
            mailer.send(from, to, "Mailer test subject", "Mailer test");
        } finally {
            ApiStack.popApi();
        }
    }
    
    public void testMD() throws IOException, ClassNotFoundException{
    	ByteArrayOutputStream bout = new ByteArrayOutputStream();
    	InputStream in = getClass().getResourceAsStream("md.dat");
    	int b1,b2;
		while( (b1 = in.read()) != -1){
			b2 = in.read();
			String s = new String(new byte[]{(byte) b1,(byte) b2});
    		int i = Integer.parseInt(s, 16);
    		bout.write(i);
    	} 
		ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
		Object obj = oin.readObject();
		assertNotNull(obj);
		
		
    }

}
