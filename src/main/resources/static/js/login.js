document.addEventListener("DOMContentLoaded", function () {
	/** ==========================
	 *      REGISTRO DE USUARIO
	 *  ========================== */

	// Elementos del formulario de registro
	const nombre = document.getElementById("nombre");
	const pApellido = document.getElementById("pApellido");
	const sApellido = document.getElementById("sApellido");
	const emailRegistro = document.getElementById("email-registro");
	const contraseñaRegistro = document.getElementById("contraseña-registro");
	const confContraseña = document.getElementById("conf-contraseña");
	const telf = document.getElementById("telf");
	const cPostal = document.getElementById("c-postal");
	const bDay = document.getElementById("bDay");
	const bMonth = document.getElementById("bMonth");
	const bYear = document.getElementById("bYear");
	const formRegistro = document.getElementById("form-registro");

	// Expresiones regulares para validación
	const passRegex = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;
	const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

	/**
	 * Compara dos contraseñas e indica si coinciden.
	 * @param {string} password1 - Contraseña principal.
	 * @param {string} password2 - Confirmación de contraseña.
	 * @param {string} msg - Mensaje de error si no coinciden.
	 * @returns {boolean} true si coinciden, false si no.
	 */
	function comparePassword(password1, password2, msg) {
		if (password1 !== password2) {
			return showError(confContraseña, msg, false);
		}
		return showSuccess(confContraseña);
	}

	/**
	 * Muestra un mensaje de error en un input.
	 * @param {HTMLElement} input - Elemento del input.
	 * @param {string} message - Mensaje de error.
	 * @returns {boolean} false.
	 */
	function showError(input, message) {
		return showMessage(input, message, false);
	}

	/**
	 * Marca un input como válido.
	 * @param {HTMLElement} input - Elemento del input.
	 * @returns {boolean} true.
	 */
	function showSuccess(input) {
		return showMessage(input, "", true);
	}

	/**
	 * Muestra un mensaje visual bajo el input.
	 * @param {HTMLElement} input - Elemento del input.
	 * @param {string} message - Mensaje a mostrar.
	 * @param {boolean} type - true si éxito, false si error.
	 * @returns {boolean} El valor del parámetro `type`.
	 */
	function showMessage(input, message, type) {
		const msg = input.parentNode.querySelector("small");
		if (msg) {
			msg.innerText = message;
		}
		input.className = type ? "success" : "error";
		return type;
	}

	/**
	 * Valida el contenido de un input contra una expresión regular.
	 * @param {HTMLInputElement} input - Campo de entrada a validar.
	 * @param {RegExp} valueRegex - Expresión regular a aplicar.
	 * @param {string} invalidMsg - Mensaje si falla.
	 * @returns {boolean} true si pasa la validación.
	 */
	function validate(input, valueRegex, invalidMsg) {
		const trimInput = input.value.trim();
		if (!valueRegex.test(trimInput)) {
			return showError(input, invalidMsg);
		}
		return true;
	}

	const EMAIL_INVALID = "El formato del correo no es correcto";
	const PASSWORD_INVALID = "Debe contener al menos un número, una mayúscula, una minúscula y al menos 8 caracteres.";
	const COMPARE_PASSWORD = "Las contraseñas no coinciden";

	// Evento al enviar el formulario
	formRegistro.addEventListener("submit", function (event) {
		event.preventDefault();
		const conReg = contraseñaRegistro.value;
		const confCon = confContraseña.value;

		let emailValid = validate(emailRegistro, emailRegex, EMAIL_INVALID);
		let passValid = validate(contraseñaRegistro, passRegex, PASSWORD_INVALID);
		let compareValid = comparePassword(conReg, confCon, COMPARE_PASSWORD);

		if (emailValid && passValid && compareValid) {
			/**
			 * @typedef {Object} User
			 * @property {string} email
			 * @property {string} password
			 * @property {string} nombre
			 * @property {string} apellidos
			 * @property {string} telefono
			 * @property {string} codigoPostal
			 * @property {string} fechaNacimiento
			 */

			/** @type {User} */
			const user = {
				email: emailRegistro.value,
				password: contraseñaRegistro.value,
				nombre: nombre.value,
				apellidos: `${pApellido.value} ${sApellido.value}`,
				telefono: telf.value,
				codigoPostal: cPostal.value,
				fechaNacimiento: `${bDay.value}-${bMonth.value}-${bYear.value}`
			};

			const formBody = Object.keys(user)
				.map(key => encodeURIComponent(key) + '=' + encodeURIComponent(user[key]))
				.join('&');

			console.log(JSON.stringify(user, null, 2));

			fetch("http://localhost:8080/api/auth/signup", {
            	method: "POST",
            	headers: { "Content-Type": "application/json" },
            	credentials: "include",
            	body: JSON.stringify(user)
            })

				.then(response => {
					if (!response.ok) {
						if (response.status === 400) {
							throw new Error("El correo ya está registrado");
						} else {
							throw new Error("Error en el registro");
						}
					}
					return response.text();
				})
				.then(formBody => {
					alert("Estas registrado correctamente " + formBody);
					window.location.href = "index.html";
				})
				.catch(error => {
					const emailSmall = emailRegistro.parentNode.querySelector("small");
					emailSmall.innerText = error.message;
					emailSmall.style.color = "red";
					console.log('Error', error);
				});
		}
	});

	/** ==========================
	 *   MANEJO DE UI: LOGIN / REGISTRO / LOGOUT
	 *  ========================== */

	const btnRegistrarme = document.getElementById("btn-registrarme");
	const divRegistro = document.getElementById("registro-div");
	const divLogin = document.getElementById("login-div");
	const banner = document.getElementById("banner");

	// Cambiar de login a registro
	btnRegistrarme.addEventListener("click", function (e) {
		e.preventDefault();
		divRegistro.style.display = "block";
		divLogin.style.display = "none";
		banner.style.display = "none";
	});

	// Cambiar de registro a login
	const btnInicio = document.getElementById("btn-inicio");
	btnInicio.addEventListener("click", function (e) {
		e.preventDefault();
		divRegistro.style.display = "none";
		divLogin.style.display = "block";
		banner.style.display = "block";
	});

	// Cerrar sesión
	document.getElementById("btn-logout").addEventListener("click", function(e) {
		e.preventDefault();
		window.location.href = "/logout";
	});
});
