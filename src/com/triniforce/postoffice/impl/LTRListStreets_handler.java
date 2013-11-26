/*
 * Copyright(C) Triniforce
 * All Rights Reserved.
 *
 */ 
package com.triniforce.postoffice.impl;

import java.util.ArrayList;

import com.triniforce.postoffice.intf.LTRListStreets;

public class LTRListStreets_handler{
    static Object process(PostMaster pm, EnvelopeCtx ctx, LTRListStreets data){
        Street ws = pm.m_rootStreet.queryPath(data.getStreetPath());
        return new ArrayList<String>(ws.getStreets().keySet());
    }
}
