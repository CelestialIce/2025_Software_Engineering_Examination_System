<template>
	<div id="app">
		<div class="container">
			<div class="user-section">
				<div class="question-section" v-if="currentQuestion">
					<div class="question-text">
						题目{{ currentQuestionIndex + 1 }}/{{ questions.length }}：{{ currentQuestion.qcontent }}
					</div>
					<div class="options">
						<!-- 是非题 -->
						<div v-if="currentQuestion.qtype === 0" class="option-container">
							<button @click="selectAnswer('A', currentQuestionIndex)">是</button>
							<button @click="selectAnswer('B', currentQuestionIndex)">否</button>
						</div>
						<!-- 单选题 -->
						<div v-if="currentQuestion.qtype === 1" class="option-container">
							<button v-for="(option, index) in currentQuestion.qchoice" :key="index"
								@click="selectAnswer(option, currentQuestionIndex)">
								{{ option }}
							</button>
						</div>
						<!-- 多选题 -->
						<div v-if="currentQuestion.qtype === 2" class="option-container">
							<button v-for="(option, index) in currentQuestion.qchoice" :key="index"
								@click="toggleAnswer(option, currentQuestionIndex)">
								{{ option }}
							</button>
						</div>
					</div>
				</div>
				<div class="navigation-buttons">
					<button @click="prevQuestion" :disabled="currentQuestionIndex === 0">上一题</button>
					<button @click="nextQuestion" :disabled="currentQuestionIndex === questions.length - 1">下一题</button>
					<button @click="submitExam" :disabled="!isAllAnswered">提交试卷</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import Fly from 'flyio/dist/npm/fly';

	export default {
		data() {
			return {
				questions: [],
				currentQuestionIndex: 0,
				userAnswers: [],
				currentQuestion: null,
				totalQuestions: 0,
				isAllAnswered: false,
			};
		},
		created() {
			this.fetchQuestions();
		},
		methods: {
			fetchQuestions() {
				const userId = localStorage.getItem("userId");
				console.log("userid from localStorage ", userId);

				if (!userId) {
					console.error('无法获取userid，用户可能未登录或未正确存储userid');
					return;
				}

				const formData = new FormData();
				formData.append("userid", parseInt(userId));
				console.log("userid after parseInt:", userId);

				const fly = new Fly();
				fly.config.headers = {
					JWT: localStorage.getItem("JWT")
				};

				fly.post('http://localhost:8080/Test/paperFetch', formData)
					.then(res => {
						if (res.data.success === 1) {
							console.log(res.data.data);
							this.questions = res.data.data;
							this.userAnswers = new Array(this.questions.length).fill(null);
							this.totalQuestions = this.questions.length;
							this.currentQuestion = this.questions[this.currentQuestionIndex];
						} else {
							alert('获取题目失败');
						}
					})
					.catch(err => {
						console.error(err);
						alert('获取题目失败');
					});
			},
			selectAnswer(answer, index) {
				this.$set(this.userAnswers, index, answer);
				this.saveAnswer(index);
			},
			toggleAnswer(option, index) {
				if (!Array.isArray(this.userAnswers[index])) {
					this.$set(this.userAnswers, index, []);
				}
				const answers = this.userAnswers[index];
				const answerIndex = answers.indexOf(option);
				if (answerIndex === -1) {
					answers.push(option);
				} else {
					answers.splice(answerIndex, 1);
				}
				this.$set(this.userAnswers, index, answers);
				this.saveAnswer(index);
			},
			saveAnswer(index) {
			                const answer = this.userAnswers[index];
			                if (answer !== null) {
			                    const userId = parseInt(localStorage.getItem('userId'));
			                    const question = this.questions[index];
			                    const formData = new FormData();
			                    formData.append("userid", userId);
								formData.append("qid",question.qid)
			                    formData.append("useranswer", Array.isArray(answer) ? answer.join('') : answer);
			                    console.log("formData ", formData);
			                    for (let pair of formData.entries()) {
			                        console.log(pair[0] + ': ' + pair[1]);
			                    }
			                    const fly = new Fly();
			                    fly.config.headers = {
			                        JWT: localStorage.getItem("JWT")
			                    };
			                    fly.post('http://localhost:8080/Test/answerSave', formData)
			                        .then(res => {
			                            if (res.data.success === 1) {
			                                console.log('答案保存成功');
			                            } else {
			                                console.log('答案保存失败');
			                            }
			                        })
			                        .catch(err => {
			                            console.error(err);
			                            console.log('答案保存失败');
			                        });
			                } else {
			                    console.log('请先选择答案');
			                }
			            },
			            prevQuestion() {
			                if (this.currentQuestionIndex > 0) {
			                    this.currentQuestionIndex--;
			                    this.currentQuestion = this.questions[this.currentQuestionIndex];
			                }
			            },
			            nextQuestion() {
			                if (this.currentQuestionIndex < this.questions.length - 1) {
			                    this.currentQuestionIndex++;
			                    this.currentQuestion = this.questions[this.currentQuestionIndex];
			                }
			            },
			            submitExam() {
			                if (this.isAllAnswered) {
			                    for (let i = 0; i < this.questions.length; i++) {
			                        this.saveAnswer(i);
			                    }
			
			                    const userId = parseInt(localStorage.getItem('userId'));
			                    const formData = new FormData();
			                    formData.append("userid", userId);
			                    const fly = new Fly();
			                    fly.config.headers = {
			                        JWT: localStorage.getItem("JWT")
			                    };
			                    fly.post('http://localhost:8080/Test/paperSubmit', formData)
			                        .then(res => {
			                            if (res.data.success === 1) {
			                                const score = res.data.data;
			                                console.log(score);
			                                alert("提交成功，您的分数是" + score.toString());
			                                window.location.href = '/index';
			                            } else {
			                                console.log('提交失败');
			                            }
			                        })
			                        .catch(err => {
			                            console.error(err);
			                            console.log('提交失败');
			                        });
			                } else {
			                    console.log('请回答所有题目后提交');
			                }
			            },
			checkIfAllAnswered() {
				this.isAllAnswered = this.userAnswers.every(answer => answer !== null);
			}
		},
		watch: {
			userAnswers: {
				deep: true,
				handler() {
					this.checkIfAllAnswered();
				}
			}
		}
	};
</script>

<style scoped>
	.container {
		padding: 10px;
		background-image: url(../../static/images/restaurant.webp);
		background-size: cover;
		width: 100vw;
		height: 100vh;
	}

	.user-section {
		padding: 20px;
	}

	.question-section {
		padding: 20px;
		background-color: rgba(255, 255, 255, 0.8);
		border-radius: 10px;
		box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
		margin-bottom: 20px;
	}

	.question-text {
		font-size: 20px;
		font-weight: bold;
		margin-bottom: 10px;
	}

	.options {
		margin-bottom: 20px;
	}

	.option-container {
		display: flex;
		flex-direction: column;
	}

	.option-box {
		display: flex;
		align-items: center;
		margin-bottom: 10px;
	}

	button {
		padding: 10px 20px;
		margin: 5px 0;
		border: none;
		border-radius: 5px;
		background-color: #f0f0f0;
		cursor: pointer;
	}

	button:hover {
		background-color: #ddd;
	}

	.navigation-buttons {
		display: flex;
		justify-content: space-between;
	}

	.navigation-buttons button {
		width: 30%;
	}
</style>