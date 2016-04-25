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
	randomWords
} from './utils/server.js'

var IndexPage = React.createClass({
	getInitialState() {
		return {
			collectedWordCount: 0,
			randomWords: ['', '', ''],
			keepWords: []
		}
	},

	componentDidMount() {
		collectedWordCount()
		.then((wordCount) => { this.setState({ collectedWordCount: wordCount }) })
		.catch((err) => { console.error(err) })

		randomWords(3)
		.then((words) => { this.setState({ randomWords:words }) })
		.catch((err) => { console.error(err) })
	},

	onCollectMoreBtnClick(evt) {
		evt.stopPropagation()

		this.setState({ collectedWordCount: '...' })

		collectMoreWords()
		.then((success) => { return collectedWordCount() })
		.then((wordCount) => { this.setState({ collectedWordCount: wordCount }) })
		.catch((err) => { console.error(err) })
	},
	
	onGetRandomWordBtnClick(evt) {
		evt.stopPropagation()

		randomWords(3)
		.then((words) => { this.setState({ randomWords: words }) })
		.catch((err) => { console.error(err) })
	},

	addToKeepWords(targetWord) {
		this.setState({ 
			keepWords: _.uniq(this.state.keepWords.concat([ targetWord ])),
			randomWords: this.state.randomWords.filter((word) => {
				return word != targetWord
			})
		})
	},

	removeFromKeepWords(targetWord) {
		this.setState({
			keepWords: this.state.keepWords.filter((word) => {
				return word != targetWord
			})
		})
	},

	render() {
		var { state } = this

		return tryCatch(() => {
			return (
				<Center>
					<Card rounded={true} width={512}>
						<RandomWords 
							words={state.randomWords} 
							onClick={this.addToKeepWords} />
						<Button 
							onClick={this.onGetRandomWordBtnClick}>
							REFRESH</Button>
						<hr />
						<KeepWords 
							words={state.keepWords}
							onClick={this.removeFromKeepWords} />
						<hr />
						<CollectingStatus 
							collectedWordCount={state.collectedWordCount} 
							onCollectMoreBtnClick={this.onCollectMoreBtnClick} />
						
					</Card>
				</Center>
			)
		})
	}
})
module.exports = IndexPage


var RandomWords = (props) => {
	return (
		<div>
		{
			props.words.map((word) => {
				return (
					<Button
						key={'random-word-' + word}
						onClick={ (evt) => {evt.stopPropagation(); props.onClick(word); } }>
						{word}</Button>
				)
			})
		}
		</div>
	)
}

var KeepWords = (props) => {
	return (
		<div>
			<div>keep words: </div>
			{
				props.words.map((word) => {
					return (
						<Button
							key={'keep-word-' + word}
							onClick={ (evt) => {evt.stopPropagation(); props.onClick(word); } }>
							{word}</Button>
					)
				})
			}
		</div>
	)
}

var CollectingStatus = (props) => {
	return (
		<div>
			<div>
				<span>수집된 단어 수: </span>
				<span>{props.collectedWordCount}</span>
			</div>
			<div>
				<Button onClick={props.onCollectMoreBtnClick}>수집하기</Button>
			</div>
		</div>
	)
}