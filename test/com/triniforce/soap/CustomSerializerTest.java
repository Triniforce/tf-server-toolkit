/* 
 * Copyright(C) Triniforce 
 * All Rights Reserved. 
 * 
 */
package com.triniforce.soap;

import java.util.ArrayList;

import com.triniforce.db.test.TFTestCase;
import com.triniforce.soap.TypeDefLibCache.PropDef.IGetSet;

@Deprecated
public class CustomSerializerTest extends TFTestCase {
	
	interface IType1{}
	interface IType2 extends IType1{}
	interface IType3{}

	public void testFind() {
		ArrayList<CustomSerializer<?,?>> srzs = new ArrayList<CustomSerializer<?,?>>();
		assertNull(CustomSerializer.find(srzs, IType1.class));

		add(srzs, IType1.class);
		assertNotNull(CustomSerializer.find(srzs, IType1.class));
		assertNull(CustomSerializer.find(srzs, IType3.class));
		assertNotNull(CustomSerializer.find(srzs, IType2.class));
	}

	private <T>void add(ArrayList<CustomSerializer<?,?>> srzs, Class<T> cls) {
		srzs.add(new CustomSerializer<T, String>(cls, String.class) {
			@Override
			public String serialize(T value) {
				return null;
			}

			@Override
			public T deserialize(String value) {
				return null;
			}
		});
	}
	
	static class O1{
		private IType1 m_v;

		public IType1 getV() {
			return m_v;
		}

		public void setV(IType1 v) {
			m_v = v;
		}
	}
	
	public void testGetGetSet() throws NoSuchMethodException, SecurityException{
		final IType1 rest = new IType1(){};
		CustomSerializer<IType1, String> ts = new CustomSerializer<IType1, String>(IType1.class, String.class){

			@Override
			public String serialize(IType1 value) {
				return "serialized";
			}

			@Override
			public IType1 deserialize(String value) {
				return rest;
			}
			
		};
		
		IGetSet gs = ts.getGetSet(O1.class.getMethod("getV", new Class[]{}), O1.class.getMethod("setV", new Class[]{IType1.class}));
		
		assertEquals("serialized", gs.get(new O1()));
		O1 o1 = new O1();
		gs.set(o1, "somestr");
		assertSame(rest, o1.getV());
		
	}

}
