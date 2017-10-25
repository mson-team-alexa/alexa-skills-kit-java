
/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package minecrafthelper;

import java.util.HashMap;
import java.util.Map;

public final class Locations {

    private static final Map<String, String> locations = new HashMap<String, String>();

    private Locations() {
    }

    static {
    	locations.put("oak wood",
        		"Oak Wood can be found in the forest biome, and scarcely scattered"
                		+ " across the winter, and jungle biome.");
    	
        locations.put("spruce wood",
        		"Spruce Wood can be found in the winter biome, and in low numbers"
                		+ " across the forest biome.");
        
        locations.put("acacia wood",
        		"Acacia Wood can only be found in the Acacia biome.");
       
        locations.put("jungle wood", 
        		"Jungle wood can only be found in the Jungle biome");
        
        locations.put("birch wood",
        		"birch wood can be found in low numbers across forest, plains, "
        		+ "and winter biomes.");
        
        locations.put("dark oak wood",
        		"Dark oaak wood can only be found in the dense forest biome, this differs"
        		+ " from the normal forest biome because of the thick dark oak trees.");
        
        
    }

    public static String get(String wood) {
        return locations.get(wood);
    }
}



