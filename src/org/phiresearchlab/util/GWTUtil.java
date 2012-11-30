package org.phiresearchlab.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.collection.PersistentBag;
import org.phiresearchlab.commsphere.domain.DomainObject;

public class GWTUtil
{
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static DomainObject makeGWTSerializable(DomainObject domainObject) {
        Class c = domainObject.getClass();
        Field[] fields = c.getDeclaredFields();
        
        for (Field field: fields) {
            Object value;
            try
            {
                value = field.get(domainObject);
                
                if (null == value)
                    continue;
                
                if (value instanceof DomainObject) {
                    makeGWTSerializable((DomainObject)value);
                    field.set(domainObject, value);
                } else if (value instanceof PersistentBag) {
                    PersistentBag bag = (PersistentBag) value;
                    List list = new ArrayList(bag.size());
                    for (Object o: bag) {
                        if (o instanceof DomainObject)
                            makeGWTSerializable((DomainObject) o);
                        list.add(o);
                        field.set(domainObject, list);
                    }
                }
            }
            catch (Exception e)
            {
                // Lets assume these ar private static declarations and ignore them for now
                continue;
            }
        }
       
        return domainObject;
    }
}
