editAreaLoader.load_syntax["latex"] = {
	'DISPLAY_NAME' : 'Latex'
	,'COMMENT_SINGLE' : {1 : '%'}
	,'COMMENT_MULTI' : {}
	,'QUOTEMARKS' : {1:'"'}
	,'KEYWORD_CASE_SENSITIVE' : false
	,'KEYWORDS' : {

	}
	,'OPERATORS' :[
		
	]
	,'DELIMITERS' :[
		'[', ']', '{', '}'
	]
	,'REGEXPS' : {
		// highlight all function with parametre(\...{...} or \...[..]{...} or \...{...}[..])
		'functParam' : {
			'search' : "(\)(\\w+)([\*|\{|\[]{1})"
			,'class' : 'functparam'
			,'modifiers' : 'g'
			,'execute' : 'before' 
		}
		// highlight all equation ($....$ or $$....$$)
		,'equation' : {
			'search' : '()(\\${1,2}[\\w+|\\n]+\\${1,2})()'
			,'class' : 'equation'
			,'modifiers' : 'g'
			,'execute' : 'before' 
		}
		// highlight all function (\...)
		/*,'functWithoutParam' : {
			'search' : ''
			,'class' : 'functwithoutparam'
			,'modifiers' : 'g'
			,'execute' : 'before' 
		}*/

	}
	,'STYLES' : {
		'COMMENTS': 'color: #AAAAAA;'
		,'QUOTESMARKS': 'color: #879EFA;'
		,'KEYWORDS' : {

			}
		,'DELIMITERS' : 'color: #2B60FF;'
		,'REGEXPS' : {
			'functparam' : 'color: #FF0000;'
			,'equation': 'color: #00FF00;'
			//,'functwithoutparam':'color: #00AAFF;'
	
		}		
	}
	
	
};
