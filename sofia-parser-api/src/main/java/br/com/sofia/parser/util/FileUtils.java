package br.com.sofia.parser.util;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;


public class FileUtils {
    
    public static Set< File > list( File dir, FileFilter filter ){
        
        Set< File > files = new HashSet< File >();
        
        File[] list = dir.listFiles();
        
        for ( File f : list ){
            if ( f.isDirectory() ){
                files.addAll( list( f, filter ) );
            } else if ( filter.accept( f ) ){
                files.add( f );
            }
        }
        
        return files;
        
    }

}
