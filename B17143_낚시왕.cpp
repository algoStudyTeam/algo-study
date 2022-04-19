#include <iostream>
#include <algorithm>
#include <cstring>
using namespace std;
struct sinfo {
	int s, d, z;
	sinfo() {}
	sinfo(int s, int d, int z) { this->s = s; this->d = d; this->z = z; }
};
int R, C, M, res;
sinfo arr[101][101], newarr[101][101];
int dx[5] = { 0,-1,1,0,0 }, dy[5] = { 0,0,0,1,-1 }, dd[5] = { 0,2,1,4,3 }; // ���Ͽ���
int main() {
	cin >> R >> C >> M;
	for (int i = 0; i < M; ++i) {
		int r, c, s, d, z;
		cin >> r >> c >> s >> d >> z;
		arr[r - 1][c - 1] = { s,d,z };
	}
	for (int t = 0; t < C; ++t) { // ���ÿ��� ��� ���� ���� ��
		for (int i = 0; i < R; ++i) {
			if (arr[i][t].z > 0) { // ���� ���� ����� ��� �ִ� ���
				res += arr[i][t].z;
				arr[i][t] = { 0,0,0 };
				break;
			}
		}
		if (t == C - 1) break; // ������ ���̸� �� ī��Ʈ �ϰ� ������ ����
		// ��� �̵�
		for (int i = 0; i < R; ++i) {
			for (int j = 0; j < C; ++j) {
				if (arr[i][j].z > 0) {
					int s = arr[i][j].s, d = arr[i][j].d, z = arr[i][j].z;
					s %= (d < 3 ? R - 1 : C - 1) * 2;
					int x = i, y = j;
					for (int k = 0; k < s; ++k) {
						x += dx[d], y += dy[d];
						if (x < 0 || x >= R || y < 0 || y >= C) { // ���� �ε����� ���� �ٲ�
							x -= dx[d], y -= dy[d];
							d = dd[d];
							x += dx[d], y += dy[d];
						}
					}
					if (newarr[x][y].z < z) { // ���� �� �� ū ��쿡�� ������� �� ����
						newarr[x][y] = { s,d,z };
					}
				}
			}
		}
		copy(&newarr[0][0], &newarr[0][0] + 101 * 101, &arr[0][0]);
		memset(newarr, 0, sizeof(newarr));
	}
	cout << res;
	return 0;
}