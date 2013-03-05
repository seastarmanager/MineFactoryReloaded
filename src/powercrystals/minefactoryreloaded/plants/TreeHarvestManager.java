package powercrystals.minefactoryreloaded.plants;

import java.util.List;

import powercrystals.core.position.Area;
import powercrystals.core.position.BlockPosition;

public class TreeHarvestManager
{
	private List<BlockPosition> _treeBlocks;
	private int _currentBlock;
	private boolean _isLeafPass;
	private boolean _isDone;
	
	private Area _treeArea;
	
	public TreeHarvestManager(Area treeArea)
	{
		_treeArea = treeArea;
		_treeBlocks = _treeArea.getPositionsBottomFirst();
		_isLeafPass = true;
	}
	
	public BlockPosition getNextBlock()
	{
		return _treeBlocks.get(_currentBlock);
	}
	
	public void moveNext()
	{
		_currentBlock++;
		if(_currentBlock >= _treeBlocks.size())
		{
			if(_isLeafPass)
			{
				_currentBlock = 0;
				_treeBlocks = _treeArea.getPositionsTopFirst();
				_isLeafPass = false;
			}
			else
			{
				_isDone = true;
			}
		}
	}
	
	public boolean getIsLeafPass()
	{
		return _isLeafPass;
	}
	
	public boolean getIsDone()
	{
		return _isDone;
	}
}