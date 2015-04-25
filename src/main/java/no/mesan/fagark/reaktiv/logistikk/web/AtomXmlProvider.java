package no.mesan.fagark.reaktiv.logistikk.web;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

@Provider
@Produces(MediaType.APPLICATION_ATOM_XML)
public class AtomXmlProvider implements MessageBodyWriter<Object> {

    @Override
    public long getSize(final Object arg0, final Class<?> arg1, final Type arg2, final Annotation[] arg3, final MediaType arg4) {
        return -1;
    }

    @Override
    public boolean isWriteable(final Class<?> arg0, final Type arg1, final Annotation[] arg2, final MediaType arg3) {
        return Feed.class.isAssignableFrom(arg0) || Entry.class.isAssignableFrom(arg0);
    }

    @Override
    public void writeTo(final Object arg0, final Class<?> arg1, final Type arg2, final Annotation[] arg3, final MediaType arg4,
            final MultivaluedMap<String, Object> arg5, final OutputStream arg6) throws IOException, WebApplicationException {
        if (arg0 instanceof Feed) {
            final Feed feed = (Feed) arg0;
            final Document<Element> document = feed.getDocument();
            document.writeTo(arg6);
        } else {
            final Entry entry = (Entry) arg0;
            final Document<Element> document = entry.getDocument();
            document.writeTo(arg6);

        }
    }

}
