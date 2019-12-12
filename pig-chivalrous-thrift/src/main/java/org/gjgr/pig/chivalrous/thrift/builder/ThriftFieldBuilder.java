package org.gjgr.pig.chivalrous.thrift.builder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.gjgr.pig.chivalrous.thrift.Generic;
import org.gjgr.pig.chivalrous.thrift.ThriftEnum;
import org.gjgr.pig.chivalrous.thrift.ThriftField;
import org.gjgr.pig.chivalrous.thrift.ThriftStruct;

public class ThriftFieldBuilder {
	
	public ThriftField buildThriftField(ThriftStructBuilder structBuilder, Field field, List<ThriftStruct> structs, List<ThriftEnum> enums) {
		ThriftField thriftField = new ThriftField();
		thriftField.setName(field.getName());
		if(field.getName().equals("__PARANAMER_DATA")) {
			return null;
		}
		Type type = field.getGenericType();
		Generic generic = Generic.fromType(field.getGenericType());
		thriftField.setGenericType(generic);
		if(type instanceof ParameterizedType) {
			structBuilder.buildStrutsByGeneric(structs, generic, enums);
		}else {
			if(generic.isEnum() || generic.isStruct()) {
				generic.setJavaClass(field.getType());
				generic.setValue(field.getType().getSimpleName());
				generic.setJavaTypeName(field.getType().getSimpleName());
				if(generic.isStruct()) {
					structBuilder.buildThriftStruct(field.getType(), structs, enums);
				}else {
					enums.add(structBuilder.buildThriftEnum(field.getType()));
				}
			}
		}
		return thriftField;
	}
}
