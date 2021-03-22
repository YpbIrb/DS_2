package utility;

import DAO.NodeDTO;
import DAO.TagDTO;
import osm.model.generated.Node;
import osm.model.generated.Tag;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {
    public static NodeDTO NodeToDTO(Node node ){
        List<TagDTO> tags = node.getTag().stream()
                .map(tag -> TagToDTO(tag, node.getId().intValue()))
                .collect(Collectors.toList());

        return
                new NodeDTO(node.getId().intValue(), node.getLat(), node.getLon(), node.getUser(), node.getUid().intValue(),
                        node.getVersion().intValue(), node.getChangeset().intValue(), new Date(node.getTimestamp().toGregorianCalendar().getTime().getTime()), tags);
    }

    public static TagDTO TagToDTO(Tag tag , long node_id){
        return new TagDTO(node_id, tag.getK(), tag.getV());
    }

}
