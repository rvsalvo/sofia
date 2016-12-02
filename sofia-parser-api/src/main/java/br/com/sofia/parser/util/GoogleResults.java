package br.com.sofia.parser.util;


import java.util.List;


public class GoogleResults {

    private List< Item > items;
    
    private Url url;
    
    private SearchInformation searchInformation;
    
    private Queries queries;
    
    
    public Url getUrl() {
    
        return url;
    }

    
    public void setUrl( Url url ) {
    
        this.url = url;
    }

    
    public SearchInformation getSearchInformation() {
    
        return searchInformation;
    }

    
    public void setSearchInformation( SearchInformation searchInformation ) {
    
        this.searchInformation = searchInformation;
    }

    
    public Queries getQueries() {
    
        return queries;
    }

    
    public void setQueries( Queries queries ) {
    
        this.queries = queries;
    }

    public List< Item > getItems() {
    
        return items;
    }

    public void setItems( List< Item > items ) {
    
        this.items = items;
    }
    
    public static class SearchInformation {

        private String formattedSearchTime;
        
        private String totalResults;

        
        public String getFormattedSearchTime() {
        
            return formattedSearchTime;
        }

        
        public void setFormattedSearchTime( String formattedSearchTime ) {
        
            this.formattedSearchTime = formattedSearchTime;
        }

        
        public String getTotalResults() {
        
            return totalResults;
        }

        
        public void setTotalResults( String totalResults ) {
        
            this.totalResults = totalResults;
        }
        
    }
    
    public static class Query {

        private String searchTerms;
        
        private long totalResults;
        
        private int count;
        
        private int startIndex;

        
        public String getSearchTerms() {
        
            return searchTerms;
        }

        
        public void setSearchTerms( String searchTerms ) {
        
            this.searchTerms = searchTerms;
        }

        
        public long getTotalResults() {
        
            return totalResults;
        }

        
        public void setTotalResults( long totalResults ) {
        
            this.totalResults = totalResults;
        }

        
        public int getCount() {
        
            return count;
        }

        
        public void setCount( int count ) {
        
            this.count = count;
        }

        
        public int getStartIndex() {
        
            return startIndex;
        }

        
        public void setStartIndex( int startIndex ) {
        
            this.startIndex = startIndex;
        }

    }
    
    public static class Queries {

        private List< Query > request;
        
        private List< Query > nextPage;

        
        public List< Query > getRequest() {
        
            return request;
        }

        
        public void setRequest( List< Query > request ) {
        
            this.request = request;
        }

        
        public List< Query > getNextPage() {
        
            return nextPage;
        }

        
        public void setNextPage( List< Query > nextPage ) {
        
            this.nextPage = nextPage;
        }

    }

    public static class Item {

        private String kind;
        
        private String title;
        
        private String htmlTitle;
        
        private String displayLink;
        
        private String link;
        
        private String snippet;
        
        public String getKind() {
        
            return kind;
        }

        
        public void setKind( String kind ) {
        
            this.kind = kind;
        }

        
        public String getTitle() {
        
            return title;
        }

        
        public void setTitle( String title ) {
        
            this.title = title;
        }

        
        public String getHtmlTitle() {
        
            return htmlTitle;
        }

        
        public void setHtmlTitle( String htmlTitle ) {
        
            this.htmlTitle = htmlTitle;
        }


        
        public String getDisplayLink() {
        
            return displayLink;
        }


        
        public void setDisplayLink( String displayLink ) {
        
            this.displayLink = displayLink;
        }


        
        public String getLink() {
        
            return link;
        }


        
        public void setLink( String link ) {
        
            this.link = link;
        }


        
        public String getSnippet() {
        
            return snippet;
        }


        
        public void setSnippet( String snippet ) {
        
            this.snippet = snippet;
        }

    }
    
    public static class Url {

        private String type;
        
        private String template;

        
        public String getType() {
        
            return type;
        }

        
        public void setType( String type ) {
        
            this.type = type;
        }

        
        public String getTemplate() {
        
            return template;
        }

        
        public void setTemplate( String template ) {
        
            this.template = template;
        }
        
    }

 }
