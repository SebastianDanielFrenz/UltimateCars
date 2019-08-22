package io.github.crashgamescrmc.UltimateCars;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class CarMetaData implements MetadataValue {

	Car car;

	public CarMetaData(Car car) {
		this.car = car;
	}

	@Override
	public boolean asBoolean() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte asByte() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double asDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float asFloat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int asInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long asLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short asShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String asString() {
		// TODO Auto-generated method stub
		return car.toString();
	}

	@Override
	public Plugin getOwningPlugin() {
		// TODO Auto-generated method stub
		return UltimateCars.plugin;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object value() {
		// TODO Auto-generated method stub
		return null;
	}

}
