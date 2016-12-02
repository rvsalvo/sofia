package br.com.sofia.parser.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Stack<T> {

    private List< T > elements;

    private int size;

    public Stack() {
        elements = new ArrayList< T >();
        size = 0;
    }
    
    public List< T > collection(){
        return elements;
    }
    
    public void reverse(){
        Collections.reverse( elements );
    }

    public T push( T element ) {

        if ( element == null )
            throw new IllegalArgumentException( "O elemento não pode ser nulo!" );

        size++;
        elements.add( element );
        
        return element;
    }


    public T peek() {

        if ( isEmpty() ){
            return null;
        }

        return elements.get( getTopPosition() );
        
    }

    public T peek( T object ) {

        if ( isEmpty() ){
            return null;
        }

        for ( T el : elements ){
            if ( el.equals( object ) ){
                return el;
            }
        }
        
        return null;
        
    }
    
    public boolean contains( T object ){
        return elements.contains( object );
    }


    /**
     * Método utilizado para retirar("destacar") um elemento desta pilha. Este
     * elemento sempre será aquele que se encontra no topo desta pilha. -Se a
     * pilha estiver vazia, retornamos null para indicar que a pilha está vazia.
     * -Se houver ao menos um elemento na pilha, o elemento que está no topo
     * será retornado, indicando o sucesso da operação
     */
    public T pop() {

        T result = peek();
        /* Se havia um elemento no topo da pilha... */
        if ( result != null ) {
            elements.remove( getTopPosition() );
            size--;
        }
        return result;
    }


    /**
     * Método utilizado para limpar todo o conteúdo da pilha.
     */
    public void clear() {

        elements.clear();
        size = 0;
        
    }


    /**
     * Método utilizado para se obter o tamanho (número de elementos) da pilha
     */
    public int getSize() {

        return size;
    }


    /**
     * Método utilizado para se obter a capacidade da pilha
     */
    public int getCapacity() {

        return elements.size();
        
    }


    /**
     * Método utilizado para verificar se a pilha está vazia. Se for o caso,
     * será retornado true, caso contrário, será retornado false.
     */
    public boolean isEmpty() {

        return size <= 0;
    }


    /**
     * Este método tem uma finalidade estritamente didática, visando facilitar o
     * entendimento do código desta classe. Este método retorna um inteiro que
     * representa a posição de elements onde se encontra o último elemento
     * inserido nesta pilha (O topo da pilha)
     */
    private int getTopPosition() {

        if ( isEmpty() )
            return 0;
        return size - 1;
    }


    public String toString() {

        StringBuilder sb = new StringBuilder( "[" );
        for ( int i = 0; i < size; i++ ) {
            sb.append( elements.get( i ).toString() );
        }
        sb.append( "]" );

        return sb.toString();
    }
}
