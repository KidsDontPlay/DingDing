package mrriegel.dingding;

import net.minecraft.util.math.BlockPos;

public class DingSave {
	public BlockPos pos;
	public int sound;

	public DingSave(BlockPos pos, int sound) {
		super();
		this.pos = pos;
		this.sound = sound;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + sound;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DingSave other = (DingSave) obj;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		if (sound != other.sound)
			return false;
		return true;
	}
}
