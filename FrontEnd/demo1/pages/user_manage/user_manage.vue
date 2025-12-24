<template>
	<view class="container">
		<!-- 筛选框 -->
		<view class="filter-box">
			<text>筛选:</text>
			<input v-model="filterKeyword" type="text" placeholder="输入关键词筛选">
			<button @click="filterUsers">筛选</button>
		</view>

		<!-- 用户列表 -->
		<view class="user-list">
			<view v-for="(user, index) in paginatedUsers" :key="user.id" class="user-item">
				<!-- 添加选项框 -->
				<checkbox-group v-model="selectedUsers" @change="handleCheckboxChange(user.userid)">
					<checkbox :value="user.id"></checkbox>
				</checkbox-group>
				<view class="user-info">
					<text><strong>ID：</strong>{{ user.userid }}</text>
					<text><strong>姓名：</strong>{{ user.username }}</text>
					<text><strong>邮箱：</strong>{{ user.email }}</text>
					<text><strong>电话：</strong>{{ user.phone }}</text>
					<text><strong>是否为管理员：</strong>{{ user.admin }}</text>
				</view>
				<!-- 其他用户信息根据需求展示-->
				<view class="actions">
					<button @click="navigateToEditUser(user)">编辑</button>
					<button @click="confirmDelete(user.userid)">删除</button>
				</view>
			</view>
		</view>

		<view class="down">
			<!-- 全选按钮 -->
			<view class="select-all">
				<checkbox @change="selectAll($event.target.checked)">全选</checkbox>
			</view>

			<!-- 删除选中按钮 -->
			<view class="delete-selected">
				<button @click="deleteSelected">删除选中</button>
			</view>
		</view>

		<!-- 分页 -->
		<view class="pagination">
			<button @click="previousPage" :disabled="currentPage === 1">上一页</button>
			<text>{{ currentPage }} / {{ totalPages }}</text>
			<button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
		</view>

		<!-- 删除确认弹窗 -->
		<view v-if="showDeleteConfirm" class="confirm-modal">
			<view class="confirm-modal-content">
				<p>真的要删除吗？</p>
				<view class="confirm-modal-actions">
					<button @click="deleteUser(currentUserToDelete)">删除</button>
					<button @click="cancelDelete">取消</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import Fly from 'flyio/dist/npm/fly';
	// 初始化 Fly 实例
	const fly = new Fly();
	export default {
		data() {
			return {
				users: [], // 用户列表数据
				currentPage: 1, // 当前页码
				pageSize: 5, // 每页显示条数
				selectedUsers: [], // 已选中用户的 id 数组
				filterKeyword: '', // 筛选关键词
				allSelected: false, // 全选状态
				showDeleteConfirm: false, // 是否显示删除确认弹窗
				currentUserToDelete: null // 当前要删除的用户ID
			};
		},
		computed: {
			// 根据筛选关键词过滤后的用户列表
			filteredUsers() {
				if (!this.filterKeyword) {
					return this.users;
				} else {
					const keyword = this.filterKeyword.toLowerCase();
					return this.users.filter(user => {
						// 检查用户ID和用户名是否匹配关键词
						const idMatch = user.userid.toString().includes(keyword);
						const nameMatch = user.username.toLowerCase().includes(keyword);
						return idMatch || nameMatch;
					});
				}
			},
			// 分页后的用户列表
			paginatedUsers() {
				const start = (this.currentPage - 1) * this.pageSize;
				const end = start + this.pageSize;
				return this.filteredUsers.slice(start, end);
			},
			// 总页数
			totalPages() {
				return Math.ceil(this.filteredUsers.length / this.pageSize);
			}
		},
		methods: {
			// 从后端获取用户数据
			fetchUsers() {
				const token = localStorage.getItem('JWT');
				if (token) {
					uni.request({
						method: 'GET',
						url: 'http://localhost:8080/User/userSelect',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT": localStorage.getItem("JWT")
						},
						success: (res) => {
							if (res.data.success === 1) {
								console.log(res.data.data)
								this.users = res.data.data;
							} else {
								console.error('获取用户列表失败:', res.data.data);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			// 筛选用户
			filterUsers() {
				// 此方法会重新计算 `filteredUsers` 的值，因为 `filterKeyword` 发生了变化
			},
			// 全选
			selectAll(checked) {
				if (checked) {
					this.selectedUsers = this.paginatedUsers.map(user => user.id);
				} else {
					this.selectedUsers = [];
				}
			},
			// 处理复选框变化
			handleCheckboxChange(userId) {
				this.currentUserToDelete = userId;
			},
			// 删除选中用户
			deleteSelected() {
				const token = localStorage.getItem('JWT');
				if (token && this.selectedUsers.length > 0) {
					uni.request({
						method: 'POST',
						url: 'http://localhost:8080/User/userDelete',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT": localStorage.getItem("JWT")
						},
						data: JSON.stringify({
							userid: this.selectedUsers // 不需要转换为字符串数组
						}),
						success: (res) => {
							if (res.data.success === 1) {
								this.users = this.users.filter(
									user => !this.selectedUsers.includes(user.userid)
								);
								this.selectedUsers = [];
							} else {
								console.error('删除用户失败:', res.data.data);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			// 显示删除确认弹窗
			confirmDelete(userId) {
				this.currentUserToDelete = userId;
				this.showDeleteConfirm = true;
			},
			// 取消删除
			cancelDelete() {
				this.showDeleteConfirm = false;
				this.currentUserToDelete = null;
			},

			// 删除单个用户
			deleteUser(userId) {
				const token = localStorage.getItem('JWT');
				if (token) {
					// 使用FormData进行添加
					var requestData = new FormData();
					// 将 userId 分割成单个字符，并逐个添加到 FormData 对象中
					const userIdStr = userId.toString();
					for (let i = 0; i < userIdStr.length; i++) {
						requestData.append("userid", userIdStr[i]);
					}

					console.log('Request data:', requestData); // 打印请求数据
					console.log('JWT token:', token); // 打印JWT token

					fly.post('http://localhost:8080/User/userDelete', requestData, {
						headers: {
							'Authorization': token,
							// 'Content-Type': 'application/json', // 不设置 Content-Type，浏览器会自动处理
							"JWT": token
						}
					}).then((res) => {
						console.log('Response:', res); // 添加调试信息
						if (res.data.success === 1) {
							this.users = this.users.filter(user => user.userid !== userId); // 使用userid而不是id
							this.showDeleteConfirm = false; // 关闭弹窗
						} else {
							console.error('删除用户失败:', res.data.data);
							// 添加更多调试信息
							console.log('Failed to delete user:', userId);
							console.log('Server response:', res.data);
						}
					}).catch((err) => {
						console.error('请求失败:', err);
					});
				}
			},
			// 跳转到编辑页面并传递用户数据
			navigateToEditUser(user) {
				const userString = encodeURIComponent(JSON.stringify(user));
				uni.navigateTo({
					url: `/pages/change_user/change_user?user=${userString}`
				});
			},
			// 上一页
			previousPage() {
				if (this.currentPage > 1) {
					this.currentPage--;
				}
			},
			// 下一页
			nextPage() {
				if (this.currentPage < this.totalPages) {
					this.currentPage++;
				}
			}
		},
		created() {
			this.fetchUsers(); // 获取用户数据
		},
		onLaunch() {
			let token = localStorage.getItem("JWT");
			if (!token) {
				uni.reLaunch({
					url: '/pages/login/login'
				});
			}
		}
	};
</script>

<style scoped>
	.container {
		padding: 20px;
		background-image: url(../../static/images/restaurant.webp);
		background-size: cover;
		width: 100vw;
		height: 100vh;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.filter-box {
		margin-bottom: 20px;
		display: flex;
		align-items: center;
	}

	.filter-box text {
		margin-right: 10px;
	}

	.filter-box input {
		flex: 1;
		padding: 5px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}

	.filter-box button {
		background-color: #85C8C5;
		border: none;
		padding: 5px 10px;
		border-radius: 5px;
		cursor: pointer;
		margin-left: 10px;
	}

	.filter-box button:hover {
		background-color: #72b0ab;
	}

	.user-list {
		flex: 1;
		overflow-y: auto;
	}

	.user-item {
		border: 1px solid #ccc;
		padding: 10px;
		margin-bottom: 10px;
		background-color: rgba(255, 255, 255, 0.8);
		border-radius: 10px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.user-info {
		flex: 1;
		margin-left: 10px;
		display: flex;
		flex-direction: column;
	}

	.user-info text {
		margin-bottom: 5px;
	}

	.actions {
		display: flex;
		flex-direction: column;
	}

	.actions button {
		width: 60px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #3498db;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
		margin-top: 5px;
	}

	.actions button:hover {
		background-color: #2980b9;
	}

	.down {
		display: flex;
		justify-content: space-between;
		margin-top: 10px;
	}

	.select-all {
		display: flex;
		align-items: center;
	}

	.delete-selected button {
		width: 100px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #e74c3c;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
	}

	.delete-selected button:hover {
		background-color: #c0392b;
	}

	.pagination {
		display: flex;
		justify-content: space-around;
		margin-top: 10px;
	}

	.pagination button {
		width: 80px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #3aacf8;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
	}

	.pagination button:hover {
		background-color: #2a8cd6;
	}

	.confirm-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 1000;
	}

	.confirm-modal-content {
		background-color: #fff;
		padding: 20px;
		border-radius: 10px;
		text-align: center;
	}

	.confirm-modal-actions {
		margin-top: 20px;
		display: flex;
		justify-content: space-around;
	}

	.confirm-modal-actions button {
		width: 80px;
		height: 30px;
		border: none;
		border-radius: 10px;
		font-size: 14px;
		cursor: pointer;
	}

	.confirm-modal-actions button:first-child {
		background-color: #e74c3c;
		color: #fff;
	}

	.confirm-modal-actions button:first-child:hover {
		background-color: #c0392b;
	}

	.confirm-modal-actions button:last-child {
		background-color: #3498db;
		color: #fff;
	}

	.confirm-modal-actions button:last-child:hover {
		background-color: #2980b9;
	}
</style>