package com.ruidev.framework.constant;

/**
 *
 * @description 分页常量
 */
public class PageConstant
{
    public static final String REQ_DATA_TYPE_JSON = "json";
    public static final String REQ_DATA_TYPE_JSONP = "jsonp";
    public static final String REQ_DATA_TYPE_XML = "xml";
    public static final String REQ_DATA_TYPE_HTML = "html";
    /**
     * 分页查询时最大单页数据量
     */
    public static final Integer MAX_PAGE_SIZE = 1000;
    /**
     * 默认单页数据量
     */
    public static final Integer DEFAULT_PAGE_SIZE = 20;
    
    public static boolean isValidDataType(String dataType){
    	return REQ_DATA_TYPE_JSON.equals(dataType) || REQ_DATA_TYPE_JSONP.equals(dataType) || REQ_DATA_TYPE_XML.equals(dataType);
    }
    
    /** sql关键词 like **/
    public static final String LIKE_SQL = "like_";

    /** sql关键词 order by **/
    public static final String ORDER_BY = " order by ";

    /** sql关键词 group by **/
    public static final String GROUP_BY = " group by ";

    /** sql比较关系 大于 等于  **/
    public static final String GR_SQL = "ge_";
    

    /** sql比较关系 或  **/
    public static final String OR_SQL = "_or_";

    /** sql比较关系 小于 等于 **/
    public static final String LE_SQL = "le_";

    /** sql比较关系 大于   **/
    public static final String GREAT_SQL = "gt_";

    /** sql比较关系 小于   **/
    public static final String LESS_SQL = "lt_";

   /** sql比较关系 不等于   **/
    public static final String NEQ_SQL = "neq_";

    /** sql比较关系 等于   **/
    public static final String EQ_SQL = "eq_";
    
    /** sql比较关系 in   **/
    public static final String IN_SQL = "in_";
    /** sql比较关系 not in   **/
    public static final String NOT_IN_SQL = "nin_";

     /** sql 过滤条件 日期  **/
    public static final String PREFIX_DATE = "f_date_";
    /** sql 过滤条件 日期  **/
    public static final String PREFIX_DATETIME = "f_datetime_";

    /** sql 过滤条件 int或者Integer  **/
    public static final String PREFIX_INT = "f_int_";

    /** sql 过滤条件 long 或者 Long  **/
    public static final String PREFIX_LONG = "f_long_";

    /** sql 过滤条件 double 或者 Double  **/
    public static final String PREFIX_DOUBLE = "f_double_";

    /** sql 排序前缀 s_   **/
    public static final String PREFIX_SORT = "s_";

    /** sql 过滤条件前缀 f_   **/
    public static final String PREFIX_FILTER = "f_";
    
    /**
     * 全文检索
     */
    public static final String PREFIX_SEARCH = "sw_";
    
    public static final String SEARCH_SEPERATOR = "_";
}
