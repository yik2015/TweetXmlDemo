package org.demo.yuyang.tweetxmldemo.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 3/24/16.
 */
public class XmlUtils {
    private final static String TAG = XmlUtils.class.getSimpleName();

    public static <T> T toBean(Class<T> type, InputStream is) {
        XStream xms = new XStream(new DomDriver("UTF-8"));
        xms.ignoreUnknownElements();
        xms.registerConverter(new MyIntConverter());
        xms.registerConverter(new MyLongConverter());
        xms.registerConverter(new MyFloatConverter());
        xms.registerConverter(new MyDoubleCoverter());
        xms.processAnnotations(type);

        T obj = null;
        try {
            obj = (T) xms.fromXML(is);
        } catch (Exception e) {
            TLog.log(TAG, "exception xml: " + e.getMessage());
        }finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    TLog.log(TAG, "exception: " + e.getMessage());
                }
            }
        }

        return obj;
    }

    public static <T> T toBean(Class<T> type, byte[] bytes) {
        if (bytes == null) return null;

        return toBean(type, new ByteArrayInputStream(bytes));
    }


    private static class MyIntConverter extends IntConverter {
        @Override
        public Object fromString(String str) {
            int value;
            try {
                value = (Integer) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class MyLongConverter extends LongConverter {
        @Override
        public Object fromString(String str) {
            long value;

            try {
                value = (Long) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class MyFloatConverter extends FloatConverter {
        @Override
        public Object fromString(String str) {
            float value;
            try {
                value = (Float) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }
    private static class MyDoubleCoverter extends DoubleConverter {
        @Override
        public Object fromString(String str) {
            double value;
            try {
                value = (Double) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }
}
