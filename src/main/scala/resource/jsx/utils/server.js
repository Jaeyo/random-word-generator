import Promise from 'promise'
import request from 'superagent'
import util from 'util'

var handleResp = function(err, resp, resolve, reject, callback) {
	if(err) {
		console.error(err.stack)
		reject(err)
		return
	}
	if(!resp.ok) {
		reject(resp.error)
		return
	}
	if(resp.body.success !== 1) {
		reject(resp.body.errmsg)
		return
	}
	var resolveData = callback(resp.body)
	resolve(resolveData)
	return
}

var get = function(url, args, callback) {
	return new Promise((resolve, reject) => {
		var req = request.get(url)
		if(args != null) req.query(args)
		req.end((err, resp) => {
			handleResp(err, resp, resolve, reject, callback)
		})
	})
}

var post = function(url, args, callback) {
	return new Promise((resolve, reject) => {
		var req = request.post(url)
		req.type('form')
		if(args != null) req.send(args)
		req.end((err, resp) => {
			handleResp(err, resp, resolve, reject, callback)
		})
	})
}


module.exports = {
	collectMoreWords: (callback) => {
		return post(
			'/random-word/collect',
			null,
			(body) => { return body.success }
		)
	},

	collectedWordCount: (callback) => {
		return get(
			'/random-word/count',
			null,
			(body) => { return body.count }
		)
	},

	randomWord: (callback) => {
		return get(
			'/random-word',
			null,
			(body) => { return body.word }
		)
	}
}