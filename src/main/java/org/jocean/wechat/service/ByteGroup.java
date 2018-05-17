package org.jocean.wechat.service;

import java.util.ArrayList;

class ByteGroup {
	ArrayList<Byte> byteContainer = new ArrayList<Byte>();

	public byte[] toBytes() {
		final byte[] bytes = new byte[byteContainer.size()];
		for (int i = 0; i < byteContainer.size(); i++) {
			bytes[i] = byteContainer.get(i);
		}
		return bytes;
	}

	public ByteGroup addBytes(final byte[] bytes) {
		for (final byte b : bytes) {
			byteContainer.add(b);
		}
		return this;
	}

	public int size() {
		return byteContainer.size();
	}
}
