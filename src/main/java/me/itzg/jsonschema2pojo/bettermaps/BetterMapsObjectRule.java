package me.itzg.jsonschema2pojo.bettermaps;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.ObjectRule;
import org.jsonschema2pojo.rules.RuleFactory;
import org.jsonschema2pojo.util.ParcelableHelper;

import java.util.Map;

/**
 * @author geof0549
 * @since Mar 2017
 */
public class BetterMapsObjectRule extends ObjectRule {
    private RuleFactory ruleFactory;

    protected BetterMapsObjectRule(RuleFactory ruleFactory, ParcelableHelper parcelableHelper) {
        super(ruleFactory, parcelableHelper);
        this.ruleFactory = ruleFactory;
    }

    @Override
    public JType apply(String nodeName, JsonNode node, JPackage _package, Schema schema) {
        final JsonNode props = node.get("properties");
        final JsonNode addlProps = node.get("additionalProperties");

        if (props == null && addlProps != null) {
            if (isSimpleType(addlProps)) {
                JType valueType = ruleFactory.getSchemaRule().apply(nodeName + "Value", addlProps, _package, schema);
                JClass mapType = _package.owner().ref(Map.class);
                mapType = mapType.narrow(_package.owner().ref(String.class), valueType.boxify());

                return mapType;

            }
        }

        return super.apply(nodeName, node, _package, schema);
    }

    private boolean isSimpleType(JsonNode instanceNode) {
        final JsonNode type = instanceNode.get("type");
        return type != null && !("object".equals(type.asText()) || "array".equals(type.asText()));
    }
}
