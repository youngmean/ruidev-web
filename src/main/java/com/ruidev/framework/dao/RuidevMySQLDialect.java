package com.ruidev.framework.dao;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class RuidevMySQLDialect extends MySQL5Dialect {
	public RuidevMySQLDialect() {
        super();
        registerFunction("regexp", new SQLFunctionTemplate(StandardBasicTypes.STRING, "?1 REGEXP ?2"));
    }
}
