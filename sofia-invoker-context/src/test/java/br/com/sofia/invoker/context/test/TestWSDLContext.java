package br.com.sofia.invoker.context.test;


import java.util.List;

import org.junit.Test;

import com.sofia.invoker.builder.AttributeFactory;
import com.sofia.invoker.context.WSDLAppContext;
import com.sofia.invoker.context.impl.WSDLAppContextFactory;
import com.sofia.invoker.exception.WSInvokerException;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.Operation;
import com.sofia.invoker.util.WSDLUtils;



/**
 * 
 * @author rsalvo
 * 
 */
public class TestWSDLContext {

    private static final String NET_NAMESPACE = "http://www.webserviceX.NET/";

    @Test
    public void testParser() {

        try {
            String wsdlUri = "http://www.byjg.com.br/site/webservice.php/ws/cep?WSDL";

            WSDLAppContext context = WSDLAppContextFactory.createWSDLContext();

            long time = System.currentTimeMillis();

            List< Operation > operations = context.parse( wsdlUri );

            System.out.println( "time: " + ( System.currentTimeMillis() - time ) );

            for ( Operation operation : operations ) {
                System.out.println( "- " + operation.getName() );
                if ( "obterCEP".equals( operation.getName() ) ) {
                    System.out.println( WSDLUtils.createXmlFromElement( WSDLUtils.createPayLoad( operation
                        .getArgument() ) ) );
                    Attribute response = context.invoke( operation );
                    System.out.println( response.toString() );
                }
            }

        }
        catch ( WSInvokerException ex ) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testParser2() {

        try {
            String wsdlUri = "http://www.webservicex.net/ConvertComputer.asmx?WSDL";

            WSDLAppContext context = WSDLAppContextFactory.createWSDLContext();

            Operation operation = context.getOperationByName( "ChangeComputerUnit", wsdlUri );

            System.out.println( "- " + operation.getName() );

            Attribute request = AttributeFactory.createComplexType( "ChangeComputerUnit", NET_NAMESPACE )
                .appendPrimitive( "ComputerValue", "1" ).appendPrimitive( "fromComputerUnit", "Megabyte" )
                .appendPrimitive( "toComputerUnit", "Kilobyte" ).createAttribute();

            System.out.println( request.toString() );

            operation.setArgument( request );
            operation.setGenerateAllNamespaces( true );

            System.out.println( WSDLUtils.createXmlFromElement( WSDLUtils.createPayLoad( operation.getArgument(), true ) ) );
            Attribute response = context.invoke( operation );
            System.out.println( response.toString() );

        }
        catch ( WSInvokerException ex ) {
            ex.printStackTrace();
        }
    }

}
