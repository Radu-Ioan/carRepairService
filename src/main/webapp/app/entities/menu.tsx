import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/car">
        <Translate contentKey="global.menu.entities.car" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/car-service">
        <Translate contentKey="global.menu.entities.carService" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/car-service-employee">
        <Translate contentKey="global.menu.entities.carServiceEmployee" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/car-repair-appointment">
        <Translate contentKey="global.menu.entities.carRepairAppointment" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
