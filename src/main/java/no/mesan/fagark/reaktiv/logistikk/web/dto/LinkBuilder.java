package no.mesan.fagark.reaktiv.logistikk.web.dto;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class LinkBuilder {


    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private final List<Link> links = new ArrayList<Link>();

    final UriBuilder baseUriBuilder;
    private String relation = "";

    public LinkBuilder(final UriBuilder baseUriBuilder) {
        super();
        this.baseUriBuilder = baseUriBuilder.clone();
    }

    public LinkBuilder withResource(final Class<?> clazz) {
        baseUriBuilder.path(clazz);
        return this;
    }

    public LinkBuilder withPath(final String route) {
        baseUriBuilder.path(route);
        return this;
    }

    public LinkBuilder withRelation(final String relation) {
        this.relation = relation;
        return this;
    }

    public LinkBuilder buildLink(final Object... objects) {
        final Link link = Link.fromUriBuilder(baseUriBuilder).rel(relation).build(objects);
        links.add(link);
        return this;
    }

    public Link getFirstLink() {
        return (links.isEmpty()) ? Link.valueOf("") : links.get(0);
    }

    public List<Link> getLinks() {
        return new ArrayList<Link>(links);
    }

}
