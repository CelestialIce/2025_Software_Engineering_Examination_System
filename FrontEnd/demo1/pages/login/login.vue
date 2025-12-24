<template>
	<view id="app">
		<!-- 容器元素 -->
		<view class="container">
			<!-- 插图部分 -->
			<view class="illustration">
				<!-- 登录表单 -->
				<view class="login-form">
					<form @submit.prevent="handleSubmit">
						<h2>登录</h2>
						<!-- 用户名标签和输入框 -->
						<view class="input-group">
							<label for="username">用户名</label>
							<input type="text" v-model="username" id="username" placeholder="用户名" required />
						</view>
						<!-- 密码标签和输入框 -->
						<view class="input-group">
							<label for="password">密码</label>
							<input type="password" v-model="password" id="password" placeholder="密码" required />
						</view>
						<!-- 记住密码复选框 -->
						<view class="checkbox-group">
							<input type="checkbox" v-model="rememberMe" id="rememberMe" />
							<label for="rememberMe">记住密码</label>
						</view>
						<!-- 按钮组 -->
						<view class="button-group">
							<button type="submit" @click="handleSubmit()">登录</button>
							<a href="#" class="register" @click.prevent="showRegister()">注册</a>
						</view>
					</form>
				</view>
			</view>

			<!-- 注册弹窗 -->
			<view v-if="showRegisterModal" class="modal-overlay" @click.self="closeRegister()">
				<view class="modal">
					<view class="modal-header">
						<h2>注册</h2>
						<button class="close-button" @click="closeRegister()">&times;</button>
					</view>
					<form @submit.prevent="handleRegister">
						<!-- 昵称 -->
						<view class="input-group">
							<label for="nickname">昵称</label>
							<input type="text" v-model="regUsername" id="nickname" placeholder="昵称" required />
						</view>
						<!-- 密码 -->
						<view class="input-group">
							<label for="reg-password">密码</label>
							<input type="password" v-model="regPassword" id="reg-password" placeholder="密码" required />
						</view>
						<!-- 确认密码 -->
						<view class="input-group">
							<label for="confirm-password">确认密码</label>
							<input type="password" v-model="confirmPassword" id="confirm-password" placeholder="确认密码"
								required />
						</view>
						<!-- 邮箱 -->
						<view class="input-group">
							<label for="email">邮箱</label>
							<input type="text" v-model="regEmail" id="regEmail" placeholder="邮箱" required />
						</view>
						<!-- 获取验证码按钮 -->
						<view class="button-group">
							<button @click="handelSendEmail()">发送验证码</button>
						</view>
						<!-- 验证码 -->
						<view class="input-group">
							<label for="captcha">验证码</label>
							<input type="text" v-model="captcha" id="captcha" placeholder="验证码" required />
						</view>
						<!-- 提交按钮 -->
						<view class="button-group">
							<button type="submit" @click="handleRegister()">注册</button>
						</view>
					</form>
				</view>
			</view>
		</view>
		<!-- 底部标识 -->
		<view class="footer">
			<text>------在线考试系统 由 思古目开诶次炯比哦 团队开发------</text>
		</view>
	</view>
</template>

<script>
	import Fly from 'flyio/dist/npm/fly';
	export default {
		data() {
			return {
				username: '',
				password: '',
				rememberMe: false,
				showRegisterModal: false,
				nickname: '',
				regUsername: '',
				regPassword: '',
				regEmail:'',
				confirmPassword: '',
				fullname: '',
				captcha: '',
				userId:'',
				isLoggedIn: false,
			};
		},
		created() {
			// 在应用启动时检查登录状态
			try {
				const token = localStorage.getItem('JWT');
				if (token) {
					// 检查JWT令牌的有效性，可以发送请求到后端验证
					this.isLoggedIn = true;
				}
			} catch (e) {
				console.error('Error parsing token from localStorage: ', e);
			}
		},
		methods: {
			handleSubmit() {
				this.login();
			},
			login() {
				var formData = new FormData();
				formData.append("userid", this.username)
				formData.append("password", this.password)
				let fly = new Fly;
				fly.post('http://localhost:8080/login', formData,)
					.then(res => {
						if (res.data.success == 1) {
							localStorage.setItem("JWT", res.data.data)
							localStorage.setItem("userId",this.username)
							this.isLoggedIn = true;
							uni.showToast({
								title: '成功登录',
								duration: 1000
							})
							uni.navigateTo({
								url: '/pages/index/index'
							})
						} else {
							uni.showToast({
								title: '登录失败',
								icon: 'none',
								duration: 1000
							})
						}
					})
			},
			showRegister() {
				this.showRegisterModal = true;
			},
			closeRegister() {
				this.showRegisterModal = false;
			},
			handelSendEmail(){
				//检查邮箱格式
				var email = this.regEmail;
				var reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
				if(!reg.test(email)){
					alert("邮箱格式不正确");
					return;
				};
				//发送验证码
				uni.request({
					method: 'POST',
					url: "http://localhost:8080/sendCode",
					header: {
						'Content-Type': 'application/json',
					},
					data: {
						"email":this.regEmail,
					},
					success: res => {
						console.log(res)
						if (res.data.success == 1) {
							uni.showToast({
								title: '发送成功',
								duration: 1002
							})
						} else {
							uni.showToast({
								title: '发送失败',
								icon: 'none',
								duration: 1002
							})
						}
					},
					fail: () => {
						console.log(this.url)
					}
				})
			},
			handleRegister() {
				// 检查密码是否匹配
				if (this.regPassword !== this.confirmPassword) {
					uni.showToast({
						title: '密码不匹配',
						icon: 'none',
						duration: 1000
					});
					return;
				}
				// 注册逻辑
				uni.request({
					method: 'POST',
					url: "http://localhost:8080/register",
					header: {
						'Content-Type': 'application/json',
					},
					data: {
						username: this.regUsername,
						password: this.regPassword,
						email:this.regEmail,
						code:this.captcha,
					},
					success: res => {
						this.userId=res.data.data;
						if (res.data.success == 1) {
							alert("注册成功,您的ID是"+this.userId)
							this.closeRegister();
						} else {
							uni.showToast({
								title: '注册失败',
								icon: 'none',
								duration: 1002
							})
						}
						this.username=this.userId;
						this.password=this.regPassword;
						this.login();
					},
					fail: () => {
						console.log(this.url)
					}
				})
			}
		},
	}
</script>

<style lang="scss">
	/* 设置全局样式 */
	body,
	html {
		height: 100%;
		margin: 0;
		font-family: Arial, sans-serif;
		display: flex;
		justify-content: center;
		align-items: center;
		background-size: cover;
		background-position: center;
	}

	/* 设置应用容器样式 */
	#app {
		display: flex;
		justify-content: center;
		align-items: center;
		width: 100%;
		height: 100%;
	}

	/* 设置页面容器样式 */
	.container {
		display: flex;
		flex-direction: column;
		background: rgba(255, 255, 255, 0.8);
		border-radius: 10px;
		box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
		overflow: hidden;
		width: 100vw;
		height: 100vh;
		align-items: center;
		justify-content: center;
		padding: 10px;
		background-image: url('../../static/images/背景7.jpg');
		background-size: cover;
		background-position: center center;
	}

	/* 设置插图样式 */
	.illustration {
		position: relative;
		width: 2000rpx;
		height: 1255rpx;
		background: url('../../static/images/loginIllustration.png') no-repeat center center;
		background-size: contain;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 50rpx;
		/* 圆角 */
		box-shadow: 0 0 100rpx rgba(0, 0, 0, 0.5);
		/* 边缘虚化 */
		overflow: hidden;
		/* 保证圆角和虚化效果 */
	}

	/* 设置登录表单样式 */
	.login-form {
		position: absolute;
		top: 16%;
		right: 6%;
		/* 将登录框向右移动 */
		padding: 80rpx;
		background-color: #fff;
		border-radius: 10px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		width: 450rpx;
		text-align: center;
	}

	/* 设置登录标题样式 */
	.login-form h2 {
		margin-bottom: 60rpx;
		color: #007BFF;
	}

	/* 设置输入框组样式 */
	.input-group {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		margin: 20rpx 0;
	}

	/* 设置标签样式 */
	.input-group label {
		margin-bottom: 10rpx;
		color: #333;
		font-size: 35rpx;
	}

	/* 设置输入框样式 */
	.login-form input[type="text"],
	.login-form input[type="password"] {
		width: 100%;
		padding: 10rpx;
		margin: 12rpx 0;
		border: 1px solid #000;
		/* 黑色边框 */
		border-radius: 25rpx;
		font-size: 20rpx;
	}

	/* 设置复选框组样式 */
	.checkbox-group {
		display: flex;
		align-items: center;
		justify-content: start;
		margin: 10px 0;
		font-size: 25rpx;
		/* 缩小字体 */
	}

	/* 设置复选框样式 */
	.checkbox-group input[type="checkbox"] {
		width: 16px;
		/* 使用 px 作为单位 */
		height: 16px;
		/* 使用 px 作为单位 */
		margin-right: 5px;
	}

	/* 设置按钮组样式 */
	.button-group {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	/* 设置按钮样式 */
	.button-group button {
		width: 100%;
		padding: 10px;
		/* 调整按钮高度 */
		margin: 10px 0;
		background-color: #007BFF;
		border: none;
		color: white;
		border-radius: 25px;
		cursor: pointer;
	}

	.button-group button:hover {
		background-color: #0056b3;
	}

	/* 设置注册链接样式 */
	.button-group .register {
		color: #007BFF;
		text-decoration: none;
		margin-top: 10px;
	}

	.button-group .register:hover {
		text-decoration: underline;
	}

	/* 弹窗遮罩层样式 */
	.modal-overlay {
		position: fixed;
		top: 0;
		left: 0;
		width: 100vw;
		height: 100vh;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		justify-content: center;
		align-items: center;
		z-index: 1000;
		/* 确保在最上层 */
	}

	/* 弹窗样式 */
	.modal {
		position: relative;
		background: #fff;
		padding: 20px;
		width: 850rpx;
		/* 修改弹窗宽度 */
		border-radius: 10px;
		box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
		z-index: 1001;
		/* 确保在最上层 */
	}

	/* 弹窗头部样式 */
	.modal-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: 1px solid #eee;
		padding-bottom: 10px;
		margin-bottom: 20px;
	}

	/* 关闭按钮样式 */
	.close-button {
		background: none;
		border: none;
		font-size: 24px;
		cursor: pointer;
		color: #999;
	}

	.close-button:hover {
		color: #333;
	}

	/* 输入框组样式 */
	.modal .input-group {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		margin-bottom: 40rpx;
		/* 增加间距 */
	}

	/* 输入框样式 */
	.modal .input-group input {
		width: 90%;
		padding: 20rpx;
		margin-top: 5px;
		border: 1px solid #ccc;
		border-radius: 5px;
		font-size: 16px;
	}

	/* 按钮组样式 */
	.modal .button-group {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	/* 按钮样式 */
	.modal .button-group button {
		width: 100%;
		padding: 12px;
		/* 调整按钮高度 */
		margin: 20px 0;
		/* 增加上下间距 */
		background-color: #007BFF;
		border: none;
		color: white;
		border-radius: 25px;
		cursor: pointer;
	}

	.modal .button-group button:hover {
		background-color: #0056b3;
	}

	/* 设置注册链接样式 */
	.modal .button-group .register {
		color: #007BFF;
		text-decoration: none;
		margin-top: 10px;
	}

	.modal .button-group .register:hover {
		text-decoration: underline;
	}

	/* 底部标识样式 */
	.footer {
		width: 100%;
		text-align: center;
		padding: 10rpx;
		position: absolute;
		bottom: 0;
		color: #007BFF;
		font-size: 35rpx;
		font-weight: bold;
	}
</style>