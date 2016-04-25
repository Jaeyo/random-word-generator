import React from 'react'
import { tryCatch } from './utils/render-util.js'
import Center from './common-comps/center.jsx'
import _ from 'underscore'
import {
	Card,
	Heading,
	Button,
	Text,
	Flex,
	Stat
} from 'rebass'
import {
	collectMoreWords,
	collectedWordCount,
	randomWord
} from './utils/server.js'

var IndexPage = React.createClass({
	getInitialState() {
		return {
			collectedWordCount: 0,
			randomWord: ''
		}
	},

	componentDidMount() {
		collectedWordCount()
		.then((wordCount) => {
			this.setState({ collectedWordCount: wordCount })
		})
		.catch((err) => {
			console.error(err)
		})
	},

	onCollectMoreBtnClick(evt) {
		evt.stopPropagation()

		this.setState({ collectedWordCount: '...' })

		collectMoreWords()
		.then((success) => {
			return collectedWordCount()
		})
		.then((wordCount) => {
			this.setState({ collectedWordCount: wordCount })
		})
		.catch((err) => {
			console.error(err)
		})
	},
	
	onGetRandomWordBtnClick(evt) {
		evt.stopPropagation()

		randomWord()
		.then((word) => {
			this.setState({ 
				randomWord: word
			})
		})
		.catch((err) => {
			console.error(err)
		})
	},

	render() {
		var { state } = this

		return tryCatch(() => {
			return (
				<Center>
					<Card rounded={true} width={512}>
						<div>
							<Stat label="수집된 단어 수" unit="개" value={state.collectedWordCount} />
							<Stat label="random-word" value={state.randomWord} />
						</div>
						<Button 
							color="white" 
							inverted={true} 
							rounded={true}
							onClick={this.onCollectMoreBtnClick}>
							수집하기
						</Button>
						<Button 
							backgroundColor="primary" 
							color="white" 
							inverted={true} 
							rounded={true}
							onClick={this.onGetRandomWordBtnClick}>
							무작위 단어
						</Button>
					</Card>
				</Center>
			)
		})
	}
})

module.exports = IndexPage