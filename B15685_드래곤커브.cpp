#include <iostream>
#include <vector>
using namespace std;
int N, res;
int arr[101][101];
int dx[4] = { 0,-1,0,1 }, dy[4] = { 1,0,-1,0 };
struct info {
	int x, y, d;
	info() {}
	info(int x, int y, int d) { this->x = x; this->y = y; this->d = d; }
};
vector<info> v;
int main() {
	cin >> N;
	for (int n = 0; n < N; ++n) {
		int x, y, d, g;
		cin >> y >> x >> d >> g;
		arr[x][y] = 1;
		x += dx[d], y += dy[d];
		arr[x][y] = 1;
		v.push_back({ x,y,d });
		for (int i = 1; i <= g; ++i) {
			int len = v.size(), xx = v[len - 1].x, yy = v[len - 1].y;
			for (int j = len - 1; j >= 0; --j) {
				int d = (v[j].d + 1) % 4;
				xx += dx[d], yy += dy[d];
				arr[xx][yy] = 1;
				v.push_back({ xx,yy,d });
			}
		}
		v.clear();
	}
	for (int i = 0; i < 100; ++i) {
		for (int j = 0; j < 100; ++j) {
			if (arr[i][j] == 1 && arr[i + 1][j] == 1 && arr[i + 1][j + 1] == 1 && arr[i][j + 1] == 1) ++res;
		}
	}
	cout << res;
	return 0;
}
