package me.itzg.jsonschema2pojo.bettermaps;

import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import org.jsonschema2pojo.Annotator;
import org.jsonschema2pojo.GenerationConfig;
import org.jsonschema2pojo.SchemaStore;
import org.jsonschema2pojo.rules.Rule;
import org.jsonschema2pojo.rules.RuleFactory;
import org.jsonschema2pojo.util.ParcelableHelper;

/**
 * @author geof0549
 * @since Mar 2017
 */
public class BetterMapsRuleFactory extends RuleFactory {
    public BetterMapsRuleFactory(GenerationConfig generationConfig, Annotator annotator, SchemaStore schemaStore) {
        super(generationConfig, annotator, schemaStore);
    }

    public BetterMapsRuleFactory() {
        super();
    }

    @Override
    public Rule<JPackage, JType> getObjectRule() {
        return new BetterMapsObjectRule(this, new ParcelableHelper());
    }
}
