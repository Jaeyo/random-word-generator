import React from 'react'
import { tryCatch } from '../utils/render-util.js'

var Center = React.createClass({
	containerStyle: {
		width: '70%',
		height: '70%',
		margin: '40px auto'
	},

	outerStyle: {
		display: 'table',
		width: '100%',
		height: '100%'
	},

	innerStyle: {
		display: 'table-cell',
		verticalAlign: 'middle',
		textAlign: 'center'
	},

	centeredStyle: {
		position: 'relative',
		display: 'inline-block'
	},

	render() {
		return tryCatch(() => {
			return (
				<div style={this.containerStyle}>
					<div style={this.outerStyle}>
						<div style={this.innerStyle}>
							<div style={this.centeredStyle}>
								{this.props.children}
							</div>
						</div>
					</div>
				</div>
			)
		})
	}
})

module.exports = Center