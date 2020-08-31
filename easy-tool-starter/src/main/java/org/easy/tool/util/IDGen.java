package org.easy.tool.util;//package com.deocean.common.util;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//
//
//public class IDGen {
//
//
//	private static final long SUFFIX=1000;
//	private static final int PREFIX=100;
//	private static final AtomicLong RANDOM=new AtomicLong(new Random().nextInt(PREFIX));
//
//
//	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyMMdd");
//	private static final AtomicLong ID = new AtomicLong (((Long.parseLong(FORMAT.format(new Date()))*PREFIX+ RANDOM.incrementAndGet()) * SUFFIX));
//
//
//	public static long next() {
//		return ID.incrementAndGet();
//	}
//
//	public static long reset() {
//		if(RANDOM.get()>PREFIX) {
//			RANDOM.set(0);
//		}
//		ID.set(((Long.parseLong(FORMAT.format(new Date()))*PREFIX+ RANDOM.incrementAndGet()) * SUFFIX)) ;
//		return ID.get();
//	}
//
//
//}