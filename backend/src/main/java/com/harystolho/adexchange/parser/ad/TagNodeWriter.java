package com.harystolho.adexchange.parser.ad;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TagNodeWriter {

	public String writeHTML(List<TagNode> tags) {
		StringBuilder sb = new StringBuilder();

		tags.forEach(tag -> appendTagTo(tag, sb));

		return sb.toString();
	}

	private void appendTagTo(TagNode tag, StringBuilder sb) {
		switch (tag.getTag()) {
		case "b":
		case "i":
		case "span":
			appendCommonTagTo(tag, sb);
			return;
		default:
			break;
		}
	}

	private void appendCommonTagTo(TagNode tag, StringBuilder sb) {
		sb.append("<" + tag.getTag() + ">");
		sb.append(tag.getContent());
		sb.append("</" + tag.getTag() + ">");
	}

}
