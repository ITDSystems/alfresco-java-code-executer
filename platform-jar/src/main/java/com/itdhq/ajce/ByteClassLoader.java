package com.itdhq.ajce;

public class ByteClassLoader extends ClassLoader {
    public Class load(byte[] bytes) {
        Class<?> cls = defineClass(null, bytes, 0, bytes.length);
        resolveClass(cls);
        return cls;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return getClass().getClassLoader().loadClass(name);
    }


}