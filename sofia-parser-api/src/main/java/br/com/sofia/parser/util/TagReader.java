package br.com.sofia.parser.util;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class TagReader {

    int tagSize = 128;

    public Tag readTag( File file ) throws IOException {
        
        RandomAccessFile raf = null;

        try {
            
            raf = new RandomAccessFile( file, "r" );
            byte[] tagData = new byte[ tagSize ];
            raf.seek( raf.length() - tagSize );
            raf.read( tagData );
            ByteBuffer bBuf = ByteBuffer.allocate( tagSize );
            bBuf.put( tagData );
            bBuf.rewind();
            
            return populateTag( bBuf );
        } finally {
            if ( raf != null ){
                raf.close();
            }
        }
    }


    private Tag populateTag( ByteBuffer bBuf ) {

        byte[] tag = new byte[ 3 ];
        byte[] tagTitle = new byte[ 30 ];
        byte[] tagArtist = new byte[ 30 ];
        byte[] tagAlbum = new byte[ 30 ];
        byte[] tagYear = new byte[ 4 ];
        byte[] tagComment = new byte[ 30 ];
        byte[] tagGenre = new byte[ 1 ];
        bBuf.get( tag ).get( tagTitle ).get( tagArtist ).get( tagAlbum ).get( tagYear ).get( tagComment )
            .get( tagGenre );
        if ( !"TAG".equals( new String( tag ) ) ) {
            throw new IllegalArgumentException( "ByteBuffer does not contain ID3 tag data" );
        }
        Tag tagOut = new Tag();
        tagOut.setTitle( new String( tagTitle ).trim() );
        tagOut.setArtist( new String( tagArtist ).trim() );
        tagOut.setAlbum( new String( tagAlbum ).trim() );
        tagOut.setYear( new String( tagYear ).trim() );
        tagOut.setComment( new String( tagComment ).trim() );
        tagOut.setGenre( tagGenre[ 0 ] );
        return tagOut;
    }

}
