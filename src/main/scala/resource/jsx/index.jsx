import React from 'react'
import { tryCatch } from './utils/render-util.js'
import Center from './common-comps/center.jsx'
import {
	Card,
	Heading,
	Button
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
			console.log({ word: word }) //DEBUG
			this.setState({ randomWord: word })
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
					<Card rounded={true} width={256}>
						<div>{state.collectedWordCount}</div>
						<Heading level={2} size={3}>수집된 단어 수</Heading>
						<Button 
							backgroundColor="primary" 
							color="white" 
							inverted={true} 
							rounded={true}
							onClick={this.onCollectMoreBtnClick}>collect more</Button>
					</Card>
					<Card rounded={true} width={256}>
						<div>{state.randomWord}</div>
						<Button 
							backgroundColor="primary" 
							color="white" 
							inverted={true} 
							rounded={true}
							onClick={this.onGetRandomWordBtnClick}>get random word</Button>
					</Card>
				</Center>
			)
		})
	}
})

module.exports = IndexPage