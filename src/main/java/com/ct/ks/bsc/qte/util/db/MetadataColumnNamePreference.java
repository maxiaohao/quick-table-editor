package com.ct.ks.bsc.qte.util.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ct.ks.bsc.qte.core.Constants;

public class MetadataColumnNamePreference {

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


    public static String findActualColumnName(ResultSetMetaData m, String typicalColumnName) throws SQLException {
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
