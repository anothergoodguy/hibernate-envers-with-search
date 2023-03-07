package com.sample.shop.service.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyValue;

/**
 * Some static funtions added to spring BeanUtils
 *
 *
 */
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

    /** log */
    private static Log log = LogFactory.getLog(BeanUtils.class);

    /**
     * Excluded properties
     */
    private static final String[] EXCLUDED_PROPERTIES = { "class" };

    /**
     * Get PropertyValues from Object
     * @param obj Object to get PropertyValues
     * @return the property values
     */
    public static PropertyValue[] getPropertyValues(Object obj) {
        PropertyDescriptor[] pds = getPropertyDescriptors(obj.getClass());
        ArrayList<PropertyValue> pvs = new ArrayList<PropertyValue>();
        List<String> excludedProperties = Arrays.asList(EXCLUDED_PROPERTIES);

        for (int i = 0; i < pds.length; i++) {
            Object value = null;
            String name = pds[i].getName();

            if (!excludedProperties.contains(name)) {
                try {
                    value = pds[i].getReadMethod().invoke(obj, (Object[]) null);
                } catch (IllegalAccessException e) {
                    log.error("Error reading property name: " + name, e);
                } catch (IllegalArgumentException e) {
                    log.error("Error reading property name: " + name, e);
                } catch (InvocationTargetException e) {
                    log.error("Error reading property name: " + name, e);
                }
                pvs.add(new PropertyValue(name, value));
            }
        }

        return (PropertyValue[]) pvs.toArray(new PropertyValue[pvs.size()]);
    }

    /**
     * Copy a property, avoid Execeptions
     * @param source source bean
     * @param dest destination bean
     * @param propertyName the propertyName
     *
     */
    public static void copyProperty(Object source, Object dest, String propertyName) {
        BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyValue pv = new PropertyValue(propertyName, wrapper.getPropertyValue(propertyName));
        // wrapper.set(dest);
        wrapper.setPropertyValue(pv);
    }

    /**
     * Set property, without trowing exceptions on errors
     * @param bean bean name
     * @param name name
     * @param value value
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            BeanWrapper wrapper = new BeanWrapperImpl(bean);
            wrapper.setPropertyValue(new PropertyValue(name, value));
        } catch (InvalidPropertyException ipe) {
            log.debug("Bean has no property: " + name);
        } catch (PropertyAccessException pae) {
            log.debug("Access Error on property: " + name);
        }
    }

    /**
     * Get property value null if none
     * @param bean beam
     * @param name name
     * @return the property value
     */
    public static Object getProperty(Object bean, String name) {
        try {
            BeanWrapper wrapper = new BeanWrapperImpl(bean);
            return wrapper.getPropertyValue(name);
        } catch (BeansException be) {
            log.error(be);
            return null;
        }
    }

    public static BooleanPredicateClausesStep<?> getPharmacy(List<UUID> pharmacyId, SearchPredicateFactory f) {
        CollectionUtils
            .emptyIfNull(pharmacyId)
            .stream()
            .forEach(uuid -> f.bool().should(f.nested().objectField("pharmacy").nest(f.match().field("pharmacy.id").matching(uuid))));
        return f.bool();
        //return f.nested().objectField("pharmacy").nest(f.match().field("pharmacy.id").matching(pharmacyId));
    }

    public static BooleanPredicateClausesStep<?> getDiagLab(List<UUID> pharmacyId, SearchPredicateFactory f) {
        CollectionUtils
            .emptyIfNull(pharmacyId)
            .stream()
            .forEach(uuid -> f.bool().should(f.nested().objectField("diagLab").nest(f.match().field("diagLab.id").matching(uuid))));
        return f.bool();
        //return f.nested().objectField("pharmacy").nest(f.match().field("pharmacy.id").matching(pharmacyId));
    }

    public static BooleanPredicateClausesStep<?> getOptionalPredicate(String unit, String fieldName, SearchPredicateFactory f) {
        if (StringUtils.isNotBlank(unit)) {
            f.bool().must(f.match().field(fieldName).matching(unit));
        }
        return f.bool();
    }

    public static <T> BooleanPredicateClausesStep<?> getOptionalPredicate(T unit, String fieldName, SearchPredicateFactory f) {
        if (null != unit) {
            f.bool().must(f.match().field(fieldName).matching(unit));
        }
        return f.bool();
    }
}
