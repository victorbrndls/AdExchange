package com.harystolho.adexchange.information;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Visitor {

	public ObjectNode visit(GlobalInformant informant);

}
