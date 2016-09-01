package com.ct.ks.bsc.qte.model;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ct.ks.bsc.qte.core.Constants;

public class ColumnInfo {

    String table_schem;
    String table_name;
    String column_name;
    int data_type;
    String type_name;
    Class<?> java_class;
    int column_size;
    int decimal_digits;
    boolean nullable;
    String remarks;
    Object column_def;
    boolean is_autoincrement;


    public String getTable_schem() {
        return table_schem;
    }


    public void setTable_schem(String table_schem) {
        this.table_schem = table_schem;
    }


    public String getTable_name() {
        return table_name;
    }


    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }


    public String getColumn_name() {
        return column_name;
    }


    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }


    public int getData_type() {
        return data_type;
    }


    public void setData_type(int data_type) {
        this.data_type = data_type;
        // also set java_class
        switch (data_type) {
        case Types.ARRAY:
            this.java_class = Object.class;
        case Types.BIGINT:
            this.java_class = Number.class;
        case Types.BINARY:
            this.java_class = Object.class;
        case Types.BIT:
            this.java_class = Number.class;
        case Types.BLOB:
            this.java_class = Object.class;
        case Types.BOOLEAN:
            this.java_class = boolean.class;
        case Types.CHAR:
            this.java_class = String.class;
        case Types.CLOB:
            this.java_class = String.class;
        case Types.DATALINK:
            this.java_class = Object.class;
        case Types.DATE:
            this.java_class = Timestamp.class;
        case Types.DECIMAL:
            this.java_class = Number.class;
        case Types.DISTINCT:
            this.java_class = Object.class;
        case Types.DOUBLE:
            this.java_class = Number.class;
        case Types.FLOAT:
            this.java_class = Number.class;
        case Types.INTEGER:
            this.java_class = Number.class;
        case Types.JAVA_OBJECT:
            this.java_class = Object.class;
        case Types.LONGNVARCHAR:
            this.java_class = String.class;
        case Types.LONGVARBINARY:
            this.java_class = Object.class;
        case Types.LONGVARCHAR:
            this.java_class = String.class;
        case Types.NCHAR:
            this.java_class = String.class;
        case Types.NCLOB:
            this.java_class = String.class;
        case Types.NULL:
            this.java_class = Object.class;
        case Types.NUMERIC:
            this.java_class = Number.class;
        case Types.NVARCHAR:
            this.java_class = String.class;
        case Types.OTHER:
            this.java_class = Object.class;
        case Types.REAL:
            this.java_class = Number.class;
        case Types.REF:
            this.java_class = Object.class;
        case Types.ROWID:
            this.java_class = Number.class;
        case Types.SMALLINT:
            this.java_class = Number.class;
        case Types.SQLXML:
            this.java_class = String.class;
        case Types.STRUCT:
            this.java_class = Object.class;
        case Types.TIME:
            this.java_class = Timestamp.class;
        case Types.TIMESTAMP:
            this.java_class = Timestamp.class;
        case Types.TINYINT:
            this.java_class = Number.class;
        case Types.VARBINARY:
            this.java_class = Object.class;
        case Types.VARCHAR:
            this.java_class = String.class;
        default:
            this.java_class = Object.class;
        }
    }


    public String getType_name() {
        return type_name;
    }


    public void setType_name(String type_name) {
        this.type_name = type_name;
    }


    public Class<?> getJava_class() {
        return java_class;
    }


    public int getColumn_size() {
        return column_size;
    }


    public void setColumn_size(int column_size) {
        this.column_size = column_size;
    }


    public int getDecimal_digits() {
        return decimal_digits;
    }


    public void setDecimal_digits(int decimal_digits) {
        this.decimal_digits = decimal_digits;
    }


    public boolean isNullable() {
        return nullable;
    }


    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }


    public String getRemarks() {
        return remarks;
    }


    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public Object getColumn_def() {
        return column_def;
    }


    public void setColumn_def(Object column_def) {
        this.column_def = column_def;
    }


    public boolean isIs_autoincrement() {
        return is_autoincrement;
    }


    public void setIs_autoincrement(boolean is_autoincrement) {
        this.is_autoincrement = is_autoincrement;
    }

    /**
     * <typical col name, all possible col names>
     */
    private static final Map<String, Set<String>> map = new HashMap<String, Set<String>>();

    static {
        map.put(Constants.META_COL_TYPICAL_NAME_TABLE_SCHEM, arrayToSet(new String[] { "TABLE_SCHEM", "TABLE_SCHEMA" }));
        map.put(Constants.META_COL_TYPICAL_NAME_TABLE_NAME, arrayToSet(new String[] { "TABLE_NAME" }));
        map.put(Constants.META_COL_TYPICAL_NAME_COLUMN_NAME, arrayToSet(new String[] { "COLUMN_NAME" }));
        map.put(Constants.META_COL_TYPICAL_NAME_DATA_TYPE, arrayToSet(new String[] { "DATA_TYPE" }));
        map.put(Constants.META_COL_TYPICAL_NAME_TYPE_NAME, arrayToSet(new String[] { "TYPE_NAME" }));
        map.put(Constants.META_COL_TYPICAL_NAME_COLUMN_SIZE, arrayToSet(new String[] { "COLUMN_SIZE",
                "CHARACTER_MAXIMUM_LENGTH", "CHARACTER_OCTET_LENGTH", "CHAR_OCTET_LENGTH" }));
        map.put(Constants.META_COL_TYPICAL_NAME_DECIMAL_DIGITS, arrayToSet(new String[] { "DECIMAL_DIGITS",
                "NUMERIC_SCALE" }));
        map.put(Constants.META_COL_TYPICAL_NAME_NULLABLE, arrayToSet(new String[] { "NULLABLE" }));
        map.put(Constants.META_COL_TYPICAL_NAME_REMARKS, arrayToSet(new String[] { "REMARKS" }));
        map.put(Constants.META_COL_TYPICAL_NAME_COLUMN_DEF, arrayToSet(new String[] { "COLUMN_DEF", "COLUMN_DEFAULT" }));
        map.put(Constants.META_COL_TYPICAL_NAME_IS_AUTOINCREMENT, arrayToSet(new String[] { "IS_AUTOINCREMENT" }));
    }


    private static Set<String> arrayToSet(String[] array) {
        Set<String> ret = new HashSet<String>();
        if (null != array && array.length > 0) {
            for (String s : array) {
                ret.add(s);
            }
        }
        return ret;
    }


    // public static Set<String> getTypicalColumnNames() {
    // return map.keySet();
    // }

    public static String getActualColName(ResultSetMetaData m, String typicalColumnName) throws SQLException {
        if (null != m) {
            int cnt = m.getColumnCount();
            Set<String> possibleNames = map.get(typicalColumnName);
            for (int i = 0; i < cnt; i++) {
                String name = m.getColumnName(i + 1);
                if (null != name && possibleNames.contains(name.toUpperCase())) {
                    return name;
                }
            }
        }
        throw new SQLException("failed to find typical column name '" + typicalColumnName
                + "' or its variants in the ResultSetMetaData object");
    }

}
