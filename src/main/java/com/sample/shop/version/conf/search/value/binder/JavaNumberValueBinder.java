package com.sample.shop.version.conf.search.value.binder;

import com.sample.shop.version.conf.search.value.bridge.JavaNumberBridge;
import org.hibernate.search.mapper.pojo.bridge.binding.ValueBindingContext;
import org.hibernate.search.mapper.pojo.bridge.mapping.programmatic.ValueBinder;

public class JavaNumberValueBinder implements ValueBinder { // <1>

    @Override
    public void bind(ValueBindingContext<?> context) { // <2>
        context.bridge(
            Number.class,
            new JavaNumberBridge(),
            context.typeFactory().asString().normalizer("case_insensitive_normalizer") // <8>
        );
    }
}
