package br.com.sofia.regex.analyzer.model;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public enum Option {
    
        OR(1),    
        PLUS(2),
        ASTERISK(3),
        QUEST(4),
        CLOSEBRACKET(5),
        HAT(6),
        LETTERCLASS(7),
        IDENT(8),
        ACTION(9),
        CLOSECLASS(10),
        DIGITCLASS(11),
        LBRACE(12),
        REGEXPEND(13),
        OPENCLASS(14),
        OPENBRACKET(15),
        CHARACTER(16),
        CLASS(17),
        DIGIT_REPEAT(18),
        SLASH(19),
        ANY(20),
        VARIABLE(21);
        
        private int op;

        Option( int op ) {

            this.op = op;
        }

        public int intValue(){
            return op;
        }  
        
        public static Option valueOf( int op ){
            for ( Option operator : Option.values() ){
                if ( operator.intValue() == op ){
                    return operator;
                }
            }
            
            return null;
        }

}
