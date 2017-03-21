This is a custom `RuleFactory` for the [jsonschema2pojo facility](https://github.com/joelittlejohn/jsonschema2pojo)
that identifies `additionalProperties` that only declare a simple type and generates a 
`Map<String,TYPE>` where `TYPE` is the boxed type of the property, such as `String`, or `Integer`.

To use it with the Maven plugin, add this artifact as a dependency of the plugin and set
the `customRuleFactory` to `me.itzg.jsonschema2pojo.moremaps.MoreMapsRuleFactory`.

For example,

```xml
<plugin>
    <groupId>org.jsonschema2pojo</groupId>
    <artifactId>jsonschema2pojo-maven-plugin</artifactId>
    <version>${jsonschema2pojo.version}</version>
    <configuration>
        <sourceDirectory>${basedir}/src/main/resources/schema</sourceDirectory>
        <!-- ...other config -->
        <customRuleFactory>me.itzg.jsonschema2pojo.bettermaps.BetterMapsRuleFactory</customRuleFactory>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>me.itzg</groupId>
            <artifactId>jsonschema2pojo-rules-bettermaps</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</plugin>

```

## Example

Given the following schema,

```json
{
  "type":"object",
  "properties": {
    "foo": {
      "type": "string"
    },
    "bars": {
      "type": "object",
      "additionalProperties": {
        "type":"string"
      }
    },
    "times": {
      "type": "object",
      "additionalProperties": {
        "type":"integer"
      }
    },
    "baz": {
       "type": "object",
       "additionalProperties": {
         "type": "object",
         "properties": {
           "key": {
             "type": "string"
           },
           "value": {
             "type": "string"
           }
         }
       }
    }
  }
}
```

The root class, called `MyClass` in this example, contains direct `Map` fields for `bars` and `times`
without the need to reference or declare additional POJO types. The field `baz` still generates
a POJO that contains fields `key` and `value` since it itself is a complex structure.


```java
public class MyClass {

    @JsonProperty("foo")
    private String foo;
    @JsonProperty("bars")
    private Map<String, String> bars;
    @JsonProperty("times")
    private Map<String, Integer> times;
    @JsonProperty("baz")
    private Baz baz;
    // ...snip
}

public class Baz {

    @JsonIgnore
    private Map<String, BazProperty> additionalProperties = 
        new HashMap<String, BazProperty>();
    // ...snip
}

public class BazProperty {

    @JsonProperty("key")
    private String key;
    @JsonProperty("value")
    private String value;
    // ...snip
}
```

The `Baz` and `BazProperty` effectively demonstrate the benefit of this rule factory.
