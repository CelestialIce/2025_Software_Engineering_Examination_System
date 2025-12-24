<<template>
	<scroll-view scroll-y="" class="container">
		<guidance></guidance>
		<!-- 用户信息 -->
		<view class="userinfo global">
			<image src="../../static/images/背景2.jpg"></image>
			<view>
				<view class="name box">{{ userData.username }}</view>
				<view class="box">注册时间: {{ userData.registerTime }}</view>
			</view>
		</view>
		<!-- 个人信息编辑区域 -->
		<view class="edit-section global">
			<form @submit.prevent="submitForm">
				<view>
					<label for="username">用户名：{{ userData.username }}</label>
					<input type="text" id="username" v-model="userData.username" placeholder="修改用户名" />
				</view>
				<view>
					<label for="email">邮箱：{{ userData.email }}</label>
					<input type="email" id="email" v-model="userData.email" placeholder="修改邮箱" />
				</view>
				<view>
					<label for="phone">电话号码：{{ userData.phone }}</label>
					<input type="tel" id="phone" v-model="userData.phone" placeholder="修改电话号码" />
				</view>
				<view class="submit">
					<button type="submit" @click="submitForm()">保存</button>
				</view>
			</form>
		</view>
		<!-- 导航列表 -->
		<view class="nav global">
			<button type="button" @click="showChangePasswordModal()">修改密码</button>
			<button type="button" @click="logout()">退出登录</button>
		</view>
		<!-- 修改密码弹窗 -->
		<view v-if="showPasswordModal" class="change-password-modal">
			<form @submit.prevent="changePassword">
				<view>
					<label for="current-password">当前密码</label>
					<input type="password" id="current-password" v-model="passwordData.currentPassword" required />
				</view>
				<view>
					<label for="new-password">新密码</label>
					<input type="password" id="new-password" v-model="passwordData.newPassword" required />
				</view>
				<view>
					<label for="confirm-password">确认新密码</label>
					<input type="password" id="confirm-password" v-model="passwordData.confirmPassword" required />
				</view>
				<view class="actions">
					<button type="button" @click="cancelChangePassword()">取消</button>
					<button type="submit">保存</button>
				</view>
			</form>
		</view>
	</scroll-view>
	</template>

	<script>
		import Fly from 'flyio/dist/npm/fly';
		export default {
			data() {
				return {
					userData: {
						username: '',
						registerTime: '',
						email: '',
						phone: ''
					},
					showPasswordModal: false,
					passwordData: {
						currentPassword: '',
						newPassword: '',
						confirmPassword: ''
					}
				};
			},
			created() {
				this.fetchUserData();
			},
			methods: {
				fetchUserData() {
					// 从后端获取用户信息
					const token = localStorage.getItem('JWT');
					if (token) {
						uni.request({
							method: 'GET',
							url: 'http://localhost:8080/User/userSelect', // 假设有一个后端接口可以获取用户信息
							header: {
								'Authorization': token,
								'Content-Type': 'application/json',
								"JWT": localStorage.getItem("JWT")
							},
							data: {
								'userid': parseInt(localStorage.getItem("userId"))
							},
							success: res => {
								if (res.data.success === 1) {
									this.userData = res.data.data[0]; // 假设返回的用户信息在data.data数组中
								} else {
									console.error('获取用户信息失败:');
								}
							},
							fail: err => {
								console.error('请求失败:', err);
							}
						});
					} else {
						console.error('JWT token不存在');
						uni.reLaunch({
							url: '/pages/login/login'
						});
					}
				},
				submitForm() {
					// 在此处添加保存用户信息的逻辑，可以通过 API 将修改后的数据提交给后端
					const token = localStorage.getItem('JWT');
					if (token) {
						// var formData = new FormData();
						// formData.append("userid", localStorage.getItem("userId"))
						// let fly = new Fly;
						// fly.config.headers={
						// 	JWT:localStorage.getItem("JWT")
						// }
						// fly.post('http://localhost:8080/User/userUpdate', formData, )
						// 	.then(res => {
						// 			if (res.data.success === 1) {
						// 				uni.showToast({
						// 					title: '保存成功',
						// 					duration: 1000
						// 				});
						// 			} else {
						// 				uni.showToast({
						// 					title: '保存失败',
						// 					icon: 'none',
						// 					duration: 1000
						// 				});
						// 				console.error('保存用户信息失败:', res.data.data);
						// 			}
						// 		} 
						// 	)
					uni.request({
						method: 'POST',
						url: 'http://localhost:8080/User/userUpdate', // 假设有一个后端接口可以更新用户信息
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT":localStorage.getItem("JWT")
						},
						data: JSON.stringify(this.userData),
						success: res => {
							if (res.data.success === 1) {
								uni.showToast({
									title: '保存成功',
									duration: 1000
								});
							} else {
								uni.showToast({
									title: '保存失败',
									icon: 'none',
									duration: 1000
								});
								console.error('保存用户信息失败:', res.data.data);
							}
						},
						fail: err => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			showChangePasswordModal() {
				this.showPasswordModal = true;
			},
			cancelChangePassword() {
				this.showPasswordModal = false;
			},
			changePassword() {
				// 修改密码逻辑
				const token = localStorage.getItem('JWT');
				if (token) {
					uni.request({
						method: 'POST',
						url: 'http://localhost:8080/User/changePassword', // 假设有一个后端接口可以更改密码
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT": localStorage.getItem("JWT")
						},
						data: this.passwordData,
						success: res => {
							if (res.data.success === 1) {
								uni.showToast({
									title: '密码修改成功',
									duration: 1000
								});
								this.cancelChangePassword();
							} else {
								uni.showToast({
									title: '密码修改失败',
									icon: 'none',
									duration: 1000
								});
								console.error('密码修改失败:', res.data.data);
							}
						},
						fail: err => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			logout() {
				// 在此处添加退出登录的逻辑，例如清空本地存储或跳转到登录页面
				localStorage.removeItem('JWT');
				uni.showToast({
					title: '退出登录',
					duration: 1000
				});
				uni.reLaunch({
					url: '/pages/login/login'
				});
			}
		}
		};
	</script>

	<style>
		.container {
			padding: 10px;
			background-image: url(../../static/images/restaurant.webp);
			background-size: cover;
			width: 100vw;
			height: 100vh;
		}

		.box {
			padding-right: 30px;
		}

		.global {
			border: 1px solid #ccc;
			padding: 20px;
			box-sizing: border-box;
		}

		.userinfo {
			display: flex;
			justify-content: space-between;
			align-items: center;
		}

		.userinfo image {
			width: 100px;
			height: 100px;
			border-radius: 50%;
		}

		.name {
			font-size: 20px;
			font-weight: bold;
		}

		.edit-section {
			margin: 10px auto;
		}

		.edit-section form {
			display: grid;
			grid-template-columns: 1fr 3fr;
			gap: 10px;
		}

		.submit button {
			width: 40%;
			/* 按钮宽度 */
			height: 45px;
			border: none;
			border-radius: 10px;
			background-color: #3498db;
			color: #fff;
			font-size: 18px;
			cursor: pointer;
		}

		.nav {
			margin: 10px auto;
			display: flex;
		}

		.nav button {
			width: 30%;
			/* 按钮宽度 */
			height: 45px;
			border: none;
			border-radius: 10px;
			background-color: #3498db;
			color: #fff;
			font-size: 18px;
			cursor: pointer;
		}

		/* 修改密码弹窗样式 */
		.change-password-modal {
			position: fixed;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
			background: rgba(255, 255, 255, 0.95);
			border-radius: 10px;
			padding: 20px;
			box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
			max-width: 400px;
			width: 90%;
			z-index: 1000;
			/* 保证弹窗在最上层 */
		}

		.change-password-modal form {
			display: flex;
			flex-direction: column;
		}

		.change-password-modal label {
			margin: 10px 0 5px;
		}

		.change-password-modal input {
			width: 95%;
			padding: 8px;
			border: 1px solid #ccc;
			border-radius: 5px;
		}

		.change-password-modal .actions {
			display: flex;
			justify-content: space-between;
			margin-top: 20px;
		}

		.change-password-modal button {
			width: 48%;
			height: 45px;
			border: none;
			border-radius: 10px;
			background-color: #3498db;
			color: #fff;
			font-size: 18px;
			cursor: pointer;
		}

		.change-password-modal button:hover {
			background-color: #2980b9;
		}

		.change-password-modal {
			pointer-events: all;
		}
	</style>