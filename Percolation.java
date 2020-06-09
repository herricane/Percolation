import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final boolean[][] grid;
    private final int gridSide;
    private final WeightedQuickUnionUF wquFull;
    private final WeightedQuickUnionUF wquTop;
    private final int virtualTop;
    private final int virtualBottom;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0.");
        }
        gridSide = n;
        int gridArea = gridSide * gridSide;
        grid = new boolean[gridSide][gridSide];
        wquFull = new WeightedQuickUnionUF(gridArea + 2);
        wquTop = new WeightedQuickUnionUF(gridArea + 1);
        virtualTop = gridArea;
        virtualBottom = gridArea + 1;
        openSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateSite(row, col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row - 1][col - 1] = true;
        openSites += 1;

        int index = flattenedIndex(row, col);
        if (row == 1) {
            wquFull.union(virtualTop, index);
            wquTop.union(virtualTop, index);
        }
        if (row == gridSide) {
            wquFull.union(virtualBottom, index);
        }
        if (isOnSite(row, col - 1) && isOpen(row, col - 1)) {
            wquFull.union(index, flattenedIndex(row, col - 1));
            wquTop.union(index, flattenedIndex(row, col - 1));
        }
        if (isOnSite(row, col + 1) && isOpen(row, col + 1)) {
            wquFull.union(index, flattenedIndex(row, col + 1));
            wquTop.union(index, flattenedIndex(row, col + 1));
        }
        if (isOnSite(row - 1, col) && isOpen(row - 1, col)) {
            wquFull.union(index, flattenedIndex(row - 1, col));
            wquTop.union(index, flattenedIndex(row - 1, col));
        }
        if (isOnSite(row + 1, col) && isOpen(row + 1, col)) {
            wquFull.union(index, flattenedIndex(row + 1, col));
            wquTop.union(index, flattenedIndex(row + 1, col));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateSite(row, col);
        return wquTop.find(virtualTop) == wquTop.find(flattenedIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquFull.find(virtualTop) == wquFull.find(virtualBottom);
    }

    private void validateSite(int row, int col) {
        if (!isOnSite(row, col)) {
            throw new IllegalArgumentException("Index is out of bound.");
        }
    }

    private boolean isOnSite(int row, int col) {
        return row > 0 && col > 0 && row <= gridSide && col <= gridSide;
    }

    private int flattenedIndex(int row, int col) {
        return (row - 1) * gridSide + col - 1;
    }
}
