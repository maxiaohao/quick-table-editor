package com.ct.ks.bsc.qte.util.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlResultSet {

    /**
     * a collection of rows of data
     */
    protected List<Object[]> _tabledata = new ArrayList<Object[]>();

    /**
     * column names of query result set
     */
    protected String[] _colNames = new String[] {};


    /**
     * Construct from given column names and list of rows of data
     *
     * @param colNames
     *            an array of column names
     * @param tabledata
     *            a table of data as List of arrays of objects
     * @see CpdAccess#query(java.sql.Connection, String, Object...)
     */
    public SqlResultSet(String[] colNames, List<Object[]> tabledata) {
        this._colNames = colNames;
        this._tabledata = tabledata;
    }


    /**
     *
     * @return the column names
     */
    public String[] getColNames() {
        return _colNames;
    }


    /**
     *
     * @return a table of data as a list of arrays of objects
     */
    public List<Object[]> getTableData() {
        return _tabledata;
    }


    /**
     *
     * @param iRow
     *            row number, 0 based
     * @return an array of objects as a row of data
     */
    public Object[] getRowAt(int iRow) {
        return this._tabledata.get(iRow);
    }


    /**
     *
     * @param iRow
     *            row number, 0 based
     * @param iCol
     *            column number, 0 based
     * @return an Object as a cell at specified row and column
     */
    public Object getCell(int iRow, int iCol) {
        return (getRowAt(iRow)[iCol]);
    }


    /**
     *
     * @param iRow
     *            row number, 0 based
     * @param szColName
     *            unique column name
     * @return an Object as a cell at specified row number and column name
     */
    public Object getCell(int iRow, String szColName) throws SQLException {
        final int colCnt = _colNames.length;
        for (int i = 0; i < colCnt; i++) {
            if (_colNames[i].equalsIgnoreCase(szColName)) {
                return getCell(iRow, i);
            }
        }
        throw new SQLException("ERROR : You are trying to get data from a field which does not exist: "
                + szColName);
    }


    /**
     *
     * @return total row count of the result set
     */
    public int getRowCount() {
        return this._tabledata.size();
    }


    /**
     *
     * @return total column count of the result set
     */
    public int getColCount() {
        return this._colNames.length;
    }
}
