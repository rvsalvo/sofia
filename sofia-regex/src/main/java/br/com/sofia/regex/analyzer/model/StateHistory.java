package br.com.sofia.regex.analyzer.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class StateHistory {
    
    private final int index;
    private final char c;
    private final NfaState holder;
    //contains start indexes of the group
    private final int[] groups;
    private Map< String, Set< Integer > > history;
    
    //contains group string 
    private final String[] groupsResult;    
    
    public StateHistory( int index, char c, NfaState holder, int[] groups, String[] groupsResult, Map< String, Set< Integer > > history ) {

        super();
        this.index = index;
        this.c = c;
        this.holder = holder;
        this.groups = ArrayUtils.clone( groups );
        this.groupsResult = (String[])ArrayUtils.clone( groupsResult );
        this.history = new HashMap< String, Set< Integer > >( history );
    }

    public int getIndex() {
    
        return index;
    }
    
    public char getC() {
    
        return c;
    }
    

    public NfaState getHolder() {
    
        return holder;
    }

    
    public int[] getGroups() {
    
        return groups;
    }

    
    public String[] getGroupsResult() {
    
        return groupsResult;
    }

    
    public Map< String, Set< Integer >> getHistory() {
    
        return history;
    }

    
    public void setHistory( Map< String, Set< Integer >> history ) {
    
        this.history = history;
    }

}
