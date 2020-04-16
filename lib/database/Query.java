package database;

/**
 * @author Tomy
 */
public class Query
{
    String query;
    private String select;
    private String from;
    private String where;
    private String order;
    private String update;
    private String set;

    public static class QueryBuilder
    {
        Query query;
        private String select = "";
        private String from = "";
        private String where = "";
        private String order = "";
        private String update = "";
        private String set = "";

        public QueryBuilder update(String update)
        {
            this.update = "UPDATE " + update + " ";
            return this;
        }

        public QueryBuilder set(String set)
        {
            this.set = "SET " + set + " ";
            return this;
        }

        public QueryBuilder from(String from) {
            this.from = "FROM " + from + " ";
            return this;
        }

        public QueryBuilder select(String select)
        {
            this.select = "SELECT " + select + " ";
            return this;
        }

        public QueryBuilder where(String where)
        {
            if(where.equals(""))
            {
                this.where = "";
            }
            else
            {
                this.where = "WHERE " + where + " ";
            }

            return this;
        }

        public QueryBuilder order(String order)
        {
            if(order.equals(""))
            {
                this.order = "";
            }
            else
            {
                this.order = "ORDER BY " + order + " ";
            }

            return this;
        }
        
        public Query query(String queryString)
        {
            return new Query(queryString);
        }

        public Query build()
        {
            return new Query(this);
        }
    }

    private Query(QueryBuilder queryBuilder)
    {
        select = queryBuilder.select;
        from = queryBuilder.from;
        where = queryBuilder.where;
        order = queryBuilder.order;
        update = queryBuilder.update;
        set = queryBuilder.set;
        
        if (update.equals(""))
        {
            query = select + from + where + order;
        }
        else
        {
            query = update + set + where + order;
        }
    }
    
    private Query(String queryString) {
        query = queryString;
    }

    public Query() {
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}