SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `waucdb`
--


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Movimiento`
--

CREATE TABLE `Movimiento` (
  `dniU` varchar(9) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` date NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `hora` time NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latitud` float(8) NOT NULL,
  `longitud` float(8) NOT NULL
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Usuario`
--

CREATE TABLE `Usuario` (
  `DNI` varchar(9) COLLATE utf8_spanish_ci NOT NULL,
  `password` varchar(8) COLLATE utf8_spanish_ci NOT NULL,
  `nombre` char(50) COLLATE utf8_spanish_ci NOT NULL,
  `telefono` int(11) NOT NULL,
  `puesto` varchar(20) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `Movimiento`
--
ALTER TABLE `Movimiento`
  ADD PRIMARY KEY (`dniU`,`fecha`,`hora`);

--
-- Indices de la tabla `Usuario`
--
ALTER TABLE `Usuario`
  ADD PRIMARY KEY (`DNI`);


--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `Movimientos`
--
ALTER TABLE `Movimiento`
  ADD CONSTRAINT `Movimiento_dniUsuario` FOREIGN KEY (`dniU`) REFERENCES `Usuario` (`DNI`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
