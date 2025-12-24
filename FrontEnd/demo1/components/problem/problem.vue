<template>
	<view>
		<!-- 题目 -->
		<view class="paper_body_problem">
			<text>{{ question.qcontent }}</text>
			<image v-if="question.qimg" :src="question.qimg" mode="aspectFill"></image>
		</view>
		<!-- 作答选项 -->
		<view class="paper_body_select">
			<!-- 单选题 -->
			<view class="select" v-if="question.qtype === 1">
				<radio-group @change="radioChange">
					<view v-for="(item, index) in question.qchoice" :key="index" class="uni-list-cell uni-list-cell-pd">
						<radio :value="item" />
						<text>{{ item }}</text>
					</view>
				</radio-group>
			</view>
			<!-- 多选题 -->
			<view class="select" v-if="question.qtype === 2">
				<checkbox-group @change="checkboxChange">
					<view v-for="(item, index) in question.qchoice" :key="index" class="uni-list-cell uni-list-cell-pd">
						<checkbox :value="item" />
						<text>{{ item }}</text>
					</view>
				</checkbox-group>
			</view>
			<!-- 是非题 -->
			<view class="select" v-if="question.qtype === 3">
				<view>
					<label class="radio">
						<radio value="true" />
						<text>正确</text>
					</label>
					<label class="radio">
						<radio value="false" />
						<text>错误</text>
					</label>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "problem",
		data() {
			return {
				question: {
					qcontent: '',
					qimg: '',
					qtype: 1, // 1: 单选题, 2: 多选题, 3: 是非题
					qchoice: [] // 选项
				}
			};
		},
		mounted() {
			this.fetchQuestion();
		},
		methods: {
			fetchQuestion() {
				const token = localStorage.getItem('JWT');
				const userId = localStorage.getItem('userid');
				if (token && userId) {
					const formData = new FormData();
					formData.append("userid", userId);
					uni.request({
						url: 'http://localhost:8080/Test/paperExistCheck',
						method: 'POST',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json'
						},
						data: formData,
						success: (res) => {
							if (res.data.success === 1) {
								const questionData = res.data.data[0]; // 假设返回的是问题列表，取第一个问题
								this.question.qcontent = questionData.qcontent;
								this.question.qimg = questionData.qimg;
								this.question.qtype = questionData.qtype;
								this.question.qchoice = questionData.qchoice || [];
							} else {
								console.error('获取问题失败:', res.data.message);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				} else {
					console.error('未能找到有效的JWT或用户ID');
				}
			},
			radioChange(event) {
				console.log('单选题选项:', event.detail.value);
			},
			checkboxChange(event) {
				console.log('多选题选项:', event.detail.value);
			}
		}
	};
</script>

<style lang="scss">
	.paper_body_problem {
		padding: 10px;
		font-size: 20px;
	}

	.paper_body_problem image {
		width: 100%;
		height: auto;
		margin-top: 10px;
	}

	.paper_body_select {
		padding-bottom: 20px;
		display: flex;
		flex-direction: column;
	}

	.select {
		padding: 10px;
		font-size: 20px;
		display: flex;
		flex-direction: column;
	}

	.uni-list-cell {
		display: flex;
		align-items: center;
		margin-bottom: 10px;
	}
</style>