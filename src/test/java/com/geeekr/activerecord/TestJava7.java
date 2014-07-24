package com.geeekr.activerecord;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.junit.Test;

public class TestJava7 {

	@Test
	public void testHandle() {
		MethodHandle mh = null;
		MethodType mt = MethodType.methodType(String.class);

		Lookup lookup = MethodHandles.lookup();

		try {
			mh = lookup.findVirtual(getClass(), "toString", mt);
			mh.invokeExact("toString");
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		return "TestJava7 [] hello world";
	}

}
