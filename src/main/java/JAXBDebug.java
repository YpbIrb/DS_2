//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7-b41 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.03.18 at 01:05:29 PM NOVT 
//

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import osm.model.generated.Bounds;
import osm.model.generated.Member;
import osm.model.generated.Nd;
import osm.model.generated.Node;
import osm.model.generated.ObjectFactory;
import osm.model.generated.Osm;
import osm.model.generated.Relation;
import osm.model.generated.Tag;
import osm.model.generated.Way;

public class JAXBDebug {


    public static JAXBContext createContext(ClassLoader classLoader)
        throws JAXBException
    {
        return JAXBContext.newInstance(Node.class, Tag.class, Nd.class, Osm.class, Bounds.class, Way.class, Relation.class, Member.class, ObjectFactory.class);
    }

}
