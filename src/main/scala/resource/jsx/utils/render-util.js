module.exports = {
	tryCatch: function(callback) {
		try {
			return callback()
		} catch(err) {
			console.error(err.stack)
		}
	}
}