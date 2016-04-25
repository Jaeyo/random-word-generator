import React from 'react'
import ReactDOM from 'react-dom'
import QS from 'query-string'
import IndexPage from './index.jsx'
require('./reset.css')

function getDOM() {
	var pathname = window.location.pathname
	var params = QS.parse(location.search)

	try {
		if(pathname === '/') {
			return (
				<IndexPage />
			)
		} else {
			throw '404'
		}
	} catch(code) {
		//TODO 404
	}
}

ReactDOM.render(
	getDOM(),
	document.getElementById('container')
)