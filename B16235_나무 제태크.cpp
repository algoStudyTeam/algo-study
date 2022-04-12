#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
using namespace std;
struct info {
	int x, y;
	info() {}
	info(int x, int y) { this->x = x; this->y = y; }
};
int N, M, K, res;
int A[11][11], val[11][11], add[11][11];
int dx[8] = { -1,-1,0,1,1,1,0,-1 }, dy[8] = { 0,1,1,1,0,-1,-1,-1 };
vector<int> tree[11][11];
queue<info> addtree;
int main() {
	cin >> N >> M >> K;
	for (int i = 0; i < N; ++i) for (int j = 0; j < N; ++j) cin >> A[i][j], val[i][j] = 5;
	for (int i = 0; i < M; ++i) {
		int a, b, c;
		cin >> a >> b >> c;
		tree[a - 1][b - 1].push_back(c);
		sort(tree[a - 1][b - 1].begin(), tree[a - 1][b - 1].end());
	}
	for (int k = 0; k < K; ++k) {
		// 1. 봄
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				for (int m = 0; m < tree[i][j].size(); ++m) {
					if (tree[i][j][m] > val[i][j]) {
						int cnt = tree[i][j].size() - m;
						while (cnt--) {
							add[i][j] += tree[i][j].back() / 2;
							tree[i][j].pop_back();
						}
						break;
					}
					else {
						val[i][j] -= tree[i][j][m];
						++tree[i][j][m];
						if (tree[i][j][m] % 5 == 0) addtree.push({ i,j });
					}
				}
				// 2. 여름
				val[i][j] += add[i][j];
				add[i][j] = 0;
			}
		}
		// 3. 가을
		while (!addtree.empty()) {
			for (int i = 0; i < 8; ++i) {
				int x = addtree.front().x + dx[i], y = addtree.front().y + dy[i];
				if (x < 0 || x >= N || y < 0 || y >= N) continue;
				tree[x][y].push_back(1);
			}
			addtree.pop();
		}
		// 4. 겨울
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				val[i][j] += A[i][j];
				sort(tree[i][j].begin(), tree[i][j].end());
			}
		}
	}
	for (int i = 0; i < N; ++i) for (int j = 0; j < N; ++j) res += tree[i][j].size();
	cout << res;
	return 0;
}
